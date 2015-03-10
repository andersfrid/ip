package Server;

import java.io.*;
import java.util.*;

public class Logger{
	
	private void log(String logMessage) throws IOException
	{
		File yourFile = new File("log.txt");
		if(!yourFile.exists()) {
		    yourFile.createNewFile();
		}
		
		PrintWriter out = new PrintWriter(yourFile);
		
		synchronized (out) {
			out.println(logMessage);
			out.flush();
			out.close();
		}		
	}
	
	public void logUser(String username) throws IOException
	{
		log(new Date()+username+" logged in");
	}
	
	public void logMessage(String username, String toUser, String message) throws IOException
	{
		if(toUser.isEmpty())
		{
			log(new Date()+":"+username+" wrote "+message);
		}
		else
		{
			log(new Date()+":"+username+" wrote to "+toUser+":"+message);
		}
	}

	public void saveAllMessage(UserMessage userMessage) throws IOException {
	 File dir = new File("Messages");
	 File[] directoryListing = dir.listFiles();
	 
	 if (directoryListing != null) {
	    for (File child : directoryListing) 
	    {
	    	saveToUser(userMessage, child);
	    }
	  }  
	}

	private void saveToUser(UserMessage userMessage, File child) throws FileNotFoundException {		
		PrintWriter out = new PrintWriter(child);
		
		synchronized (out) {
			out.println(new Date()+":"+userMessage.getUsername()+" wrote "+userMessage.getMessage());
			out.flush();
			out.close();
		}
	}

	public void saveToMessage(String toUser, UserMessage userMessage) throws IOException {
		File yourFile = new File("Messages/"+toUser+".txt");
		if(!yourFile.exists()) {
		    yourFile.createNewFile();
		}

		saveToUser(userMessage, yourFile);
	}

	public LinkedList<String> readMessages(String username) throws IOException
	{
		LinkedList<String> messages = new LinkedList<String>();
		File file = new File("Messages/"+username+".txt");
		
		if(file.exists()) 
		{
			int count = 0;
			
		    FileReader fReader = new FileReader(file);
		    BufferedReader bReader = new BufferedReader(fReader);
		    
		    while(bReader.read() != -1)
		    {
		    	String s = bReader.readLine();
		    	messages.add(s);
		    	count++;
		    	
		    	if(count > 5)
		    	{
		    		break;
		    	}
		    }
		}
		
		return messages;
	}

	public class MessageUpdater extends Thread
	{
		private User user;
		
		@Override
		public void run() 
		{
			Logger logger = new Logger();
			try 
			{
				LinkedList<String> messages = logger.readMessages(user.Username);
				
				if(messages.size() > 0)
				{
					synchronized (user.OutStream) {
						user.OutStream.writeObject(messages);
						user.OutStream.flush();
					}				
				}
				
				interrupt();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
}
