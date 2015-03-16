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

public class User implements Serializable{
	public String Username;
	public InetAddress Address;
	public ObjectOutputStream OutStream;
	public ObjectInputStream InStream;
	public File messageFile;
	
	public User(String name,InetAddress address, ObjectOutputStream outStream,ObjectInputStream inStream) throws IOException
	{
		Username = name;
		Address = address;
		OutStream = outStream;
		InStream = inStream;
		
		CheckMessageFile();
		new MessageUpdater().start();
	}
	
	public User(String name,File file)
	{
		Username = name;
		
		messageFile = file;
	}
	
	public User(String name)
	{
		Username = name;
	}

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(OutStream != null)
		{
			try {
				MessageUpdater update = new MessageUpdater();
				update.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		messageFile = file;
	}

	public void Update(InetAddress address, ObjectOutputStream outStream,
			ObjectInputStream inStream) {
		
		InStream = inStream;
		OutStream = outStream;
		Address = address;
		
		new MessageUpdater().start();
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
					
					synchronized (OutStream) {
						OutStream.writeObject(workedList);
						OutStream.flush();
					}
				}

				interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public UserMessage reworkMessages(String message) throws ParseException
		{
			String[] array = message.split(";");
			Date date = new SimpleDateFormat("yy-MM-dd HH:mm").parse(array[0]);
			 
			UserMessage newMess = new UserMessage(array[1],array[3],null,date);
			
			return newMess;
		}
	}	
}
