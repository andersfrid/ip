package Server;

import javax.swing.Icon;

import Interfaces.iMessage;

public class UserMessage implements iMessage {
	private String Username;
	private String TextMessage;
	private Icon Image;
	private boolean IsPublic;
	private String ToUser;
	
	@Override
	public String getMessage() {
		return TextMessage;
	}
	@Override
	public void setMessage() {
		
	}
	@Override
	public Icon getImage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setImage() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean IsPublic() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String ToUser() {
		// TODO Auto-generated method stub
		return null;
	}
}
