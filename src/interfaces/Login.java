package interfaces;

import java.io.Serializable;

public class Login implements Serializable{
	public String Username;
	
	public Login(String name)
	{
		Username = name;
	}
}
