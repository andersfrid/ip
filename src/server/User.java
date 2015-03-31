package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.text.*;
/**
 * 
 * @author Hiplobbe
 */
public class User implements Serializable{
	public String Username;
	public InetAddress Address;
	public ObjectOutputStream OutStream;
	public ObjectInputStream InStream;
	public File messageFile;
	/**
	 * Constructor for a new user.
	 * 
	 * @param name Username 
	 * @param address InetAddress of the client socket.
	 * @param outStream ObjectOutputStream of the client socket.
	 * @param inStream ObjectInputStream of the client socket.
	 * @throws IOException
	 */
	public User(String name,InetAddress address, ObjectOutputStream outStream,ObjectInputStream inStream) throws IOException
	{
		Username = name;
		Address = address;
		OutStream = outStream;
		InStream = inStream;
		
		CheckMessageFile();
	}
	/**
	 * Constructor for an offline user.
	 * 
	 * @param name Username
	 * @param file The message file for the user.
	 */
	public User(String name,File file)
	{
		Username = name;
		
		messageFile = file;
	}
	/**
	 * Simple constructor for user.
	 * 
	 * @param name Username
	 */
	public User(String name)
	{
		Username = name;
	}
	/**
	 * Check if there is any old messages for the user.
	 */
	private void CheckMessageFile() {
		File dir = new File("Messages");

		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File file = new File("Messages/" + Username + ".txt");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		messageFile = file;
	}
	/**
	 * Updates the user with new connectivity info.
	 * 
	 * @param address InetAddress of the client socket.
	 * @param outStream ObjectOutputStream of the client socket.
	 * @param inStream ObjectInputStream of the client socket.
	 */
	public void Update(InetAddress address, ObjectOutputStream outStream,
			ObjectInputStream inStream) {
		
		InStream = inStream;
		OutStream = outStream;
		Address = address;
		
		new MessageUpdater().start();
	}
	/**
	 * Disconnects the user.
	 */
	public void Disconnect()
	{
		OutStream = null;
		InStream = null;
	}
	/**
	 * Class used to update the user with previous messages.
	 * 
	 * @author Hiplobbe
	 */
	public class MessageUpdater extends Thread {
		@Override
		public void run() {
			try {		
				Logger log = new Logger();				
				ArrayList<String> unWorkedList = log.checkMessages(Username);			
				
				if (unWorkedList.size() > 0) {
					LinkedList<UserMessage> workedList = new LinkedList<UserMessage>();
					
					for(String s : unWorkedList)
					{
						workedList.add(reworkMessages(s));
					}
					
					if(OutStream != null)
					{
						synchronized (OutStream) {
							OutStream.writeObject(workedList);
							OutStream.flush();
						}
					}
				}

				this.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * Takes an unworked string representation of an old message and makes it into a userMessage object.
		 * 
		 * @param message The string representation of the message.
		 * @return An userMessage object.
		 * @throws ParseException
		 */
		public UserMessage reworkMessages(String message) throws ParseException
		{
			String[] array = message.split(";");
			Date date = new SimpleDateFormat("yy-MM-dd HH:mm").parse(array[0]);
			 
			UserMessage newMess = new UserMessage(array[1],array[3],null,date);
			
			return newMess;
		}
	}	
}
