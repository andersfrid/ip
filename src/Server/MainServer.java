package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;

public class MainServer {
	private ServerSocket connSocket;
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
					
					String username =  inStream.readUTF();
					
					User newUser = new User(username, new ObjectOutputStream(socket.getOutputStream()));
					
					userList.add(newUser);
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
