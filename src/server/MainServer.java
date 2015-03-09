package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;

import interfaces.iMessage;

public class MainServer {
	private ServerSocket connSocket;
	private int outPort = 3550;
	private ClientListener listener = new ClientListener();
	private LinkedList<User> userList = new LinkedList<User>();
	
	public MainServer() throws IOException
	{
		connSocket = new ServerSocket(3550);
		
		listener.start();
	}
	
	public static void main(String[] args) {
		
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
				if(userMessage.IsPublic())
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
					User u = findUser(userList,userMessage.getUsername());
					
					try {
						sendToSpecific(u,userMessage);
						
						logger.saveToMessage(u.Username,userMessage);
					} 
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

		private User findUser(LinkedList<User> userList, String username) {
			for(User u : userList)
			{
				if(u.Username == username)
				{
					return u;
				}
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
					socket = connSocket.accept();
					
					ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
					
					Object obj =  inStream.readObject();
					
					if(obj instanceof String)
					{
						ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
						
						User newUser = new User((String)obj,socket.getInetAddress(), stream);
										
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
				} catch (ClassNotFoundException e) {
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
