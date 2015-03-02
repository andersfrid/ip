package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;

import Interfaces.iMessage;

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

		public MessageHandler(iMessage message) {
			userMessage = (UserMessage) message;
		}
		
		@Override
		public void run() {
			while(!isInterrupted())
			{
				
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
						//TODO Start MessageHandler.
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
				ObjectOutputStream stream = u.OutStream;
				
				stream.writeObject(userList);
				stream.flush();
			}
		}
	}
}
