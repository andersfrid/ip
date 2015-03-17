package server;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class UserList implements Serializable,Iterable<User>
{
	private ArrayList<User> list = new ArrayList<User>();
	private int Size = 0;
	
	public UserList()
	{
		fillUserList();
	}
	
	private void fillUserList() {
		File dir = new File("Messages");

		if (!dir.exists()) {
			dir.mkdir();
		}

		File[] directoryListing = dir.listFiles();

		if (directoryListing != null) {
			for (File child : directoryListing) {
				String name = child.getName();
				
				int pos = name.lastIndexOf(".");
				if (pos > 0) {
					name = name.substring(0, pos);
				}
				
				list.add(new User(name,child));
			}
		}
	}
	
	public int size()
	{
		return Size;
	}
	
	public User findUser(String username) throws IOException {
		for (User u : list) {
			if (u.Username.equals(username)) {
				return u;
			}
		}

		File file = new File("Messages/" + username + ".txt");
		if (!file.exists()) {
			try {
				Logger logger = new Logger();
				logger.logError(username);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		else
		{
			return new User(username,file);
		}	
	}

	public void add(User user) {
		list.add(user);
	}
	
	public void remove(User user)
	{
		list.remove(user);
	}
	
	@Override
	public Iterator<User> iterator() {
        return list.iterator(); 
	}

	public boolean exists(User newUser) throws IOException {
		for(User u : list)
		{
			if(u.Username.equals(newUser.Username))
			{
				return true;
			}
		}
		
		return false;
	}

	public void updateUser(User newUser) {
		for(User u : list)
		{
			if(u.Username.equals(newUser.Username))
			{
				u.Update(newUser.Address,newUser.OutStream,newUser.InStream);
			}			
		}
	}

	public ArrayList<String> getUserList() {
		ArrayList<String> returnList = new ArrayList<String>();
		
		for(User u : list)
		{
			if(u.OutStream != null)
			{
				returnList.add(u.Username+":Online");
			}
			else 
			{
				returnList.add(u.Username+":Offline");
			}
		}
		
		return returnList;
	}

	public ArrayList<User> getActiveUsers() {
		ArrayList<User> returnList = new ArrayList<User>();
		
		for(User u : list)
		{
			if(u.OutStream != null)
			{
				returnList.add(u);
			}
		}
		
		return returnList;
	}
}
