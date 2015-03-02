package Interfaces;

import javax.swing.Icon;

public interface iMessage {
	public String getMessage();
	public void setMessage(String message);
	
	public Icon getImage();
	public void setImage(Icon image);
	
	public boolean IsPublic();
	
	public String ToUser();
	public void setTo(String username);
}
