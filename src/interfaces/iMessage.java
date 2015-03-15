package interfaces;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;

public interface iMessage{
	
	public String getUsername();
	public void setUsername(String username);
	
	public String getMessage();
	public void setMessage(String message);
	
	public Icon getImage();
	public void setImage(Icon image);
	
	public ArrayList<String> ToUser();
	
	public void setTo(String username);
}
