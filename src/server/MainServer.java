package server;

import interfaces.Login;
import interfaces.iMessage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

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
	}

	public static void main(String[] args) throws IOException {
		new MainServer();
		new ClientGUI(new ClientController("127.0.0.1", 3520));
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
				if(User.InStream == null)
				{
					interrupt();
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
					}
					
					if(obj instanceof iMessage)
					{
						UserMessage userMessage = (UserMessage)obj;
						
						if (userMessage.ToUser() == null) {
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
								} 
								catch (IOException e) {
									e.printStackTrace();
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

		private void sendToAll(UserMessage message) throws IOException {
			for (User u : userList) {
				synchronized (u.OutStream) {
					u.OutStream.writeObject(message);
					u.OutStream.flush();
				}

//				logger.logMessage(message.getUsername(), null,
//						message.getMessage());
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
					
					ArrayList<String> arr = new ArrayList<String>();
					if(obj instanceof String){
//						obj = (String)obj;
//						System.out.println(obj);
						
						User newUser = new User((String)obj, socket.getInetAddress(), outStream,inStream);
						
						if (!userList.exists(newUser)) 
						{
							userList.add(newUser);
						} 
						else 
						{
							userList.updateUser(newUser);
						}
						
						for(User u : userList)
						{
							if(u.OutStream == null)
							{
								arr.add(u.Username+":Online");
							}
							else 
							{
								arr.add(u.Username+":Offline");
							}
						}
						
//						for(int i = 0; i < 6; i++){
//							arr.add("HEJ"+i);
//						}
						
//						arr = userList.getUserList();
						
						System.out.println("Skriver ut från server : " + arr);
						outStream.writeObject(arr);
						outStream.flush();
						
						new MessageHandler(newUser);
					}
					if(obj instanceof iMessage)
					{
						System.out.print("ANDREAS FEJS!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void updateClientLists(ArrayList<Login> list, User user) throws IOException {
			synchronized (user.OutStream) {
				user.OutStream.writeObject(list);
				user.OutStream.flush();
			}
		}
	}
}
