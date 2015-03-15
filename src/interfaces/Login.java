package interfaces;

import java.io.Serializable;

public class Login implements Serializable{
	private String username;
	
	public Login(String name)
	{
		username = name;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return username;
	}
}
