package client;

import interfaces.Client;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClientController_bak {
	private Client client;
	private GUIMess ui = new GUIMess();

	private void showClientUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Client");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ui);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

//	public void FileChooser(){
//		JFileChooser chooser = new JFileChooser();
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//				"JPG & GIF Images", "jpg", "gif");
//		chooser.setFileFilter(filter);
//		int returnVal = chooser.showOpenDialog(parent);
//		if(returnVal == JFileChooser.APPROVE_OPTION) {
//			System.out.println("You chose to open this file: " +
//					chooser.getSelectedFile().getName());
//		}
//	}	
	public ClientController_bak() {
		client.setClientController(this);
		showClientUI();
	}

	public void put(String name) {
		try {
			client.put(name);
		} catch(IOException e) {
			newResponse(e.toString());
		}
	}
	

	public void get(String name) {
		try {
			client.get(name);
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void list() {
		try {
			client.list();
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void remove(String name) {
		try {
			client.remove(name);
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void exit() {
		try {
			client.exit();
		} catch (IOException e) {
			newResponse(e.toString());
		}		
	}

	public void newResponse(final String response) {
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() {
	//			ui.setResponse(response);
			}
		});
	}

}
