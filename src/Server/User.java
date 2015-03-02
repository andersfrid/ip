package Server;

import java.io.ObjectOutputStream;

public class User {
	private String Username;
	private ObjectOutputStream OutStream;
	
	public User(String name, ObjectOutputStream stream)
	{
		Username = name;
		stream = OutStream;
	}
}
