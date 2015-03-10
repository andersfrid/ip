package client;

import interfaces.Client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ClientController {
	private ClientUser client;
	private GUIMess gui = new GUIMess(this);
	
//	private void showClientUI() {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//			    JFrame frame = new JFrame("Messenger");
//			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			    frame.add(gui);
//			    frame.pack();
//			    frame.setVisible(true);
//			}
//		});
//	}
	
	public ClientController(Client client){
		this.client = (ClientUser) client; //vet inte varför den måste castas
		client.setClientController(this);
//		showClientUI();	
	}
	//Lägger in nytt meddelande på servern!!!
	public void put(String text){
		try{
			client.put(text);
		}catch(IOException e){
			System.out.println(e); //ändra så error kommer i rutan senare kanske.
		}
	}
	//Hämtar gammalt meddelande om texten stämmer med det som skrivits in!!!
	public void get(String text){
		try{
			client.get(text);
		}catch(IOException e){
			System.out.println(e); // ändra så error kommer i rutan sen kanske?
		}
	}
	// Hämtar värdet som laggts till på servern!!!
	public void list(){
		try{
			client.list();
		}catch(IOException e){
			System.out.println(e);
		}
	}
	//Tar bort möget som inte ska finnas.
	public void remove(String text){
		try{
			client.remove(text);
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	// får meddelande från client som skickas till metod i gui som uppdaterar i vår konversation.
	public void newResponse(final String mess) {
		SwingUtilities.invokeLater(new Runnable() { // behövs ej för ClientA
			public void run() {
				gui.showMessage(mess);
			}
		});
	}
	
	public static void main(String[]args) throws IOException{
		Client clientUser = new ClientUser("127.0.0.1",3550);
		new ClientController(clientUser);
	}
	
}


//public void FileChooser(){
//JFileChooser chooser = new JFileChooser();
//FileNameExtensionFilter filter = new FileNameExtensionFilter(
//		"JPG & GIF Images", "jpg", "gif");
//chooser.setFileFilter(filter);
//int returnVal = chooser.showOpenDialog(parent);
//if(returnVal == JFileChooser.APPROVE_OPTION) {
//	System.out.println("You chose to open this file: " +
//			chooser.getSelectedFile().getName());
//}
//}



//david