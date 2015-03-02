package Interfaces;

import javax.swing.Icon;

public interface iMessage {
	public String getMessage();
	public void setMessage();
	
	public Icon getImage();
	public void setImage();
	
	public boolean IsPublic();
	public String ToUser();
}
