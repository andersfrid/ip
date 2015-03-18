package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;

import client.ClientController;
import client.ClientGUI;

public class MainServer {
	private ServerSocket connSocket;
	private int outPort = 3520;
	private ClientListener listener = new ClientListener();
	private UserList userList = new UserList();

	public MainServer() throws IOException {
		connSocket = new ServerSocket(outPort);
		listener.start();
		new UpdateClientLists().start();
	}

	public static void main(String[] args) throws IOException {
//		new MainServer();
		new ClientGUI(new ClientController("10.2.21.12", 3520));
	}

	/**
	 * Sends the given message to the users chosen, or to all users.
	 * 
	 * @author hiplobbe
	 */
	private class MessageHandler extends Thread {
		private User User;
		private Logger logger = new Logger();

		public MessageHandler(User u) {
			User = u;
			
			this.start();
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				if(User.InStream == null || User.OutStream == null)
				{
//					new UpdateClientLists().start();				
					this.interrupt();
				}
				else
				{	
					Object obj = new Object();
					try 
					{
						obj = User.InStream.readObject();
					} 
					catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
						this.interrupt();
					}
					
					if(obj instanceof UserMessage)
					{
						UserMessage userMessage = (UserMessage)obj;
						System.out.println(userMessage.getMessage());
						
						if (userMessage.ToUser().size() == 0) {
							try {
								sendToAll(userMessage);
								logger.saveAllMessage(userMessage);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} 
						else if (userMessage.ToUser() != null) {
							ArrayList<String> listOfUsers = userMessage.ToUser();
		
							for (String user : listOfUsers) {
								try {
									User u = userList.findUser(user);
		
									if (u != null && u.OutStream != null) {
		
										sendToSpecific(u, userMessage);
		
										logger.saveToMessage(u.Username, userMessage);
									} 
									else if(u != null)
									{
										logger.saveToMessage(u.Username, userMessage);
									}
									else
									{
										logger.logError(user);
									}
								} 
								catch (IOException e) {
									e.printStackTrace();
//									new UpdateClientLists().start();				
									this.interrupt();
								}
							}
						}
					}
				}
			}
		}

		private void sendToSpecific(User to, UserMessage message)
				throws IOException {
			synchronized (to.OutStream) {
				to.OutStream.writeObject(message);
				to.OutStream.flush();
			}

			logger.logMessage(message.getUsername(), to.Username,
					message.getMessage());
		}

		private void sendToAll(UserMessage message){
			for (User u : userList.getActiveUsers()) {
				synchronized (u.OutStream) {
					try {
						u.OutStream.writeObject(message);
						u.OutStream.flush();
					} 					
					catch (IOException e)
					{
						e.printStackTrace();
						u.Update(u.Address, null, null);
					}
				}
			}
		}
	}

	/**
	 * Handles the messages coming in, and waits for users to log in.
	 * 
	 * @author hiplobbe
	 */
	private class ClientListener extends Thread {
		private Socket socket;
		private Logger logger = new Logger();

		@Override
		public void run() {
			System.out.println("Startar Server");
			while (!isInterrupted()) {
				try {
					socket = connSocket.accept();

					ObjectOutputStream outStream = new ObjectOutputStream(
							socket.getOutputStream());

					ObjectInputStream inStream = new ObjectInputStream(
							socket.getInputStream());

					
					Object obj = inStream.readObject();
					

					if(obj instanceof String){
						
						User newUser = new User((String)obj, socket.getInetAddress(), outStream,inStream);
						
						if (!userList.exists(newUser)) 
						{
							userList.add(newUser);					
						} 
						else 
						{
							userList.updateUser(newUser);
						}
						
//						new UpdateClientLists().start();
						
						logger.logUser(newUser.Username);
						
//						System.out.println("Skriver ut frÃ¥n server : " + userList.getUserList());
//						synchronized (outStream) {
//							outStream.writeObject(userList.getUserList());
//							outStream.flush();
//						}				
						
						new MessageHandler(newUser);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class UpdateClientLists extends Thread  
	{			
		@Override
		public void run() {
			while(!interrupted())
			{
				ArrayList<User> activeUsers = userList.getActiveUsers();
				ArrayList<String> list = userList.getUserList();
				
				for(User user : activeUsers)
				{
					synchronized (user.OutStream) {
						try 
						{
							user.OutStream.writeObject(list);
							user.OutStream.flush();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}					
					}
				}
				try {
					this.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}			
	}
}
