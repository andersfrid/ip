package server;

import interfaces.iMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;

public class UserMessage implements iMessage,Serializable{
	private String Username;
	private String TextMessage;
	private Icon Image;
//	private boolean IsPublic;
	private ArrayList<String> ToUser;
	
	public UserMessage(String username, String message, Icon image){
		this.Username = username;
		this.TextMessage = message;
		this.Image = image;
	}
	
	@Override
	public String getMessage() {
		return TextMessage;
	}

	@Override
	public String getUsername() {
		return Username;
	}
	
	@Override
	public void setUsername(String username) {
		Username = username;
	}

	@Override
	public void setMessage(String message) {

	}

	@Override
	public Icon getImage() {
		return Image;
	}

	@Override
	public void setImage(Icon image) {
		
	}

	@Override
	public void setTo(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> ToUser() {
		return ToUser;
	}

}
