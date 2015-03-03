package client;

public class ClientController {
	private ClientUser client;
	private GUIMess gui;
	
	public ClientController(){
		
	}
	
	//Användaren loggar in
	public void login(String username){
		System.out.println("I controller med användarnamn: "+username);
		
		
		ClientUser client = new ClientUser(this);
		GUIMess gui = new GUIMess("IP (inloggad som "+username+")", this);
	}

	
}
