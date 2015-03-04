package Server;

import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class User {
	public String Username;
	public InetAddress Address;
	public ObjectOutputStream OutStream;
	
	public User(String name,InetAddress address, ObjectOutputStream stream)
	{
		Username = name;
		Address = address;
		stream = OutStream;
	}
}
