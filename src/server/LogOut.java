package server;

import java.io.Serializable;
/**
 * Message to be sent when logging in or out.
 * 
 * @author Hiplobbe
 */
public class LogOut implements Serializable {
	public String Username;
//	public boolean InOut; // True if logging in.
	
	public LogOut(String name)
	{
		Username = name;
	}
}
