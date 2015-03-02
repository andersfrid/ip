package client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Interfaces.Client;

public class ClientController {
	private Client client;
	private ClientUI ui = new ClientUI(this);
	
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
	

	
	public ClientController(Client client) {
		this.client = client;
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
				ui.setResponse(response);
			}
		});
	}

}
