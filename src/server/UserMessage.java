package server;

import javax.swing.Icon;

import interfaces.iMessage;

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
	public String getUsername() {
		return Username;
	}

	@Override
	public void setUsername() {
		
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
	public boolean IsPublic() {
		return IsPublic;
	}

	@Override
	public String ToUser() {
		return ToUser;
	}

	@Override
	public void setTo(String username) {
		// TODO Auto-generated method stub
		
	}

}
