package server;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.Icon;

public class UserMessage implements Serializable{
	private String Username;
	private String TextMessage;
	private Icon Image;
//	private boolean IsPublic;
	private ArrayList<String> ToUser = new ArrayList<String>();
	
	public UserMessage(String username, String message, Icon image){
		this.Username = username;
		this.TextMessage = message;
		this.Image = image;
	}
	
	
	public String getMessage() {
		return TextMessage;
	}

	public String getUsername() {
		return Username;
	}
	
	public void setUsername(String username) {
		Username = username;
	}

	public void setMessage(String message) {
		this.TextMessage = message;
	}

	public Icon getImage() {
		return Image;
	}

	public void setImage(Icon image) {
		this.Image = image;
	}

	public void setTo(String username) {
		this.ToUser.add(username);
	}

	public ArrayList<String> ToUser() {
		return ToUser;
	}

}
