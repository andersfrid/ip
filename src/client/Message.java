package client;

import java.util.LinkedList;

import javax.swing.Icon;

import interfaces.iMessage;

public class Message implements iMessage{
	private String username;
	private String message;
	private Icon image = null; 
	private LinkedList<String> toUsers;
	
	public Message(String username, String message, Icon image){
		this.username = username;
		this.message = message;
		this.image = image;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {	
		this.message = message;
	}

	public Icon getImage() {
		return image;
	}

	public void setImage(Icon image) {
		this.image = image;
	}

	public LinkedList<String> ToUser() {
		return toUsers;
	}

	public void setTo(String username) {
		toUsers.add(username);
	}

}
