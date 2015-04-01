package server;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
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
	/**
	 * Fills the user list with offline users.
	 */
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
	/**
	 * Returns number of items in the list.
	 * 
	 * @return The size of the list.
	 */
	public int size()
	{
		return Size;
	}
	/**
	 * Finds a user inside the list.
	 * 
	 * @param username The username of the user to be found.
	 * @return The user, or null if no user is found.
	 * @throws IOException
	 */
	public User findUser(String username) throws IOException {
		for (User u : list) {
			if (u.Username.equals(username)) {
				return u;
			}
		}

		return null;
	}
	/**
	 * Adds a user to the list.
	 * 
	 * @param user
	 */
	public void add(User user) {
		list.add(user);
	}
	/**
	 * Removes a user from the list.
	 * @param user
	 */
	public void remove(User user)
	{
		list.remove(user);
	}

	@Override
	/**
	 * Returns the iterator of the list.	
	 */
	public Iterator<User> iterator() {
        return list.iterator(); 
	}
	/**
	 * Checks if user exists inside the list, and returns a boolean value.
	 * 
	 * @param user The user to check.
	 * @return true or false.(True if found) 
	 * @throws IOException
	 */
	public boolean exists(User user) throws IOException {
		for(User u : list)
		{
			if(u.Username.equals(user.Username))
			{
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Updates a user with new connectivity info.
	 * 
	 * @param user The user to be updated.
	 */
	public void updateUser(User user) {
		for(User u : list)
		{
			if(u.Username.equals(user.Username))
			{
				u.Update(user.Address,user.OutStream,user.InStream);
			}			
		}
	}
	/**
	 * Returns the usernames of the user in the list.
	 * 
	 * @return ArrayList<String> filled with the usernames.
	 */
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
	/**
	 * Return users with an active OutputObjectStream
	 * 
	 * @return An ArrayList<User> object filled with the users.
	 */
	public ArrayList<User> getActiveUsers() {
		ArrayList<User> returnList = new ArrayList<User>();
		
		for(User u : list)
		{
			try
			{
				if(u.OutStream != null)
				{
					returnList.add(u);
				}
				else
				{
					u.Update(null, null, null);
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
		return returnList;
	}
}
