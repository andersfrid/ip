package server;

import interfaces.iMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;

import client.ClientController;
import client.ClientGUI;

public class MainServer {
	private ServerSocket connSocket;
	private int outPort = 3520;
	private ClientListener listener = new ClientListener();
	private LinkedList<User> userList = new LinkedList<User>();
	
	public MainServer() throws IOException
	{
		connSocket = new ServerSocket(3520, 0, InetAddress.getByName(null));
		listener.start();
	}
	
	public static void main(String[] args) throws IOException {
		new MainServer();
		new ClientGUI(new ClientController("127.0.0.1",3520));
	}
	
	private class MessageHandler extends Thread
	{
		private UserMessage userMessage;
		private Logger logger = new Logger();

		public MessageHandler(iMessage message) {
			userMessage = (UserMessage) message;
		}
		
		@Override
		public void run() {
			while(!isInterrupted())
			{
				if(userMessage.ToUser().isEmpty())
				{
					try {
						sendToAll();	
						logger.saveAllMessage(userMessage);
					} 
					catch (IOException e) {
						e.printStackTrace();
					}		
				}
				else if(!userMessage.ToUser().isEmpty())
				{
					LinkedList<String> listOfUsers = userMessage.ToUser();
					
					for(String user : listOfUsers)
					{
						User u = findUser(user);
						
						if(u != null)
						{
							try {
								sendToSpecific(u,userMessage);
								
								logger.saveToMessage(u.Username,userMessage);
							} 
							catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				interrupt();
			}
		}

		private void sendToSpecific(User u, UserMessage message) throws IOException {
			synchronized (u.OutStream) {
				u.OutStream.writeObject(message);
				u.OutStream.flush();
			}
					
			logger.logMessage(userMessage.getUsername(),u.Username,userMessage.getMessage());
		}

		private User findUser(String username) {
			for(User u : userList)
			{
				if(u.Username == username)
				{
					return u;
				}
			}

			Logger logger = new Logger();
			
			try 
			{
				logger.logError(username);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		private void sendToAll() throws IOException {
			for(User u : userList)
			{
				synchronized (u.OutStream) {
					u.OutStream.writeObject(userMessage);
					u.OutStream.flush();
				}
				
				logger.logMessage(userMessage.getUsername(),null,userMessage.getMessage());
			}
		}
	}
	
	private class ClientListener extends Thread
	{
		private Socket socket;
		
		@Override
		public void run() {
			while(!isInterrupted())
			{
				try 
				{
					System.out.println("startar asdadasdasd");
					socket = connSocket.accept();
					
					ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

					Object obj =  inStream.readUTF();
					System.out.println(obj);
					
					if(obj instanceof String)
					{
						//TODO Kolla om användare redan finns!
						//TODO Bry dig inte om dom är online eller inte.
						User newUser = new User((String)obj,socket.getInetAddress(), outStream);
										
						userList.add(newUser);
						updateClientLists();	
					}
					else if(obj instanceof iMessage)
					{
						MessageHandler handler = new MessageHandler((iMessage)obj);
						handler.start();
					}
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void updateClientLists() throws IOException {			
			for(User u : userList)
			{
				synchronized (u.OutStream) {
					u.OutStream.writeObject(userList);
					u.OutStream.flush();
				}
			}
		}
	}
}
