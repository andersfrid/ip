package client;

import interfaces.Login;
import interfaces.iMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ClientController {
	private String username;
	private ArrayList<String> usersOnServer;

	private GUIMess gui;

	private Socket socket;
	private String ip;
	private int port;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Listener listener;

	public ClientController(String ip, int port) {
		this.ip = ip;
		this.port = port;
		connect();

	}

	public void setUser(String username) {
		this.username = username;
	}
	
	public String getUser(){
		return username;
	}

	public ArrayList<String> getListOnUsers() {
		return usersOnServer;
	}

	public void connect() {
		try {
			socket = new Socket(this.ip, this.port);
			socket.setSoTimeout(5000);
			System.out.println(socket.toString());
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("kan koppla med server");
		} catch (Exception e) {
			System.err.println("Client kan inte koppla med server!");
			System.out.println(e);
		}
	}

	public void startGUIMess() {
		try {
			oos.writeObject(this.username);
			oos.flush();

		} catch (Exception e) {
			System.err.println("Kan inte skicka användarnamn till Server!");
		}

		try {
			System.out.println("första i try");
			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				System.out.println("är en ArrayList!");
				obj = (ArrayList<String>) obj;
				System.out.println(obj);

				usersOnServer = (ArrayList<String>) obj;
				System.out.println("Får något!");
				usersOnServer.toString();

				listener = new Listener();

			}
			
		} catch (Exception e) {
			System.err.println("Kan inte få tillbaka en lista");
			System.err.println(e);
		}

		gui = new GUIMess(this);
		printMessage(new Message("Kuckelemuu", "Hej jag heter kuckelemuuu", null));
		printMessage(new Message("Andreas", "Hej jag heter Andreas", null));
		printMessage(new Message("Emelie", "Hej jag heter Emelie", null));
		printMessage(new Message("Pelle", "Hej jag heter Pelle", new ImageIcon("/Users/Desktop/gubbe.jpg")));
		
	}

	public void sendMessage(String mess, Icon image, ArrayList<String> toUsers) {

		Message newMess = new Message(this.username, mess, image);

		if (toUsers != null) {
			for (int i = 0; i < toUsers.size(); i++) {
				newMess.setTo(toUsers.get(i));
			}
		}

		System.out.println("kommer skickas till" + toUsers);

		try {
			oos.writeObject(newMess);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printMessage(Message mess) {
		if (mess instanceof iMessage) {
			String author, message;
			Icon image;

			author = mess.getUsername();
			message = mess.getMessage();
			image = mess.getImage();

			gui.writeMessage(author, message, image);
		} else {
			System.err.println("Tyvärr är inte meddelandet ett iMessage-obj");
		}

	}

	/**
	 * Tråden lyssnar efter meddelande från server.
	 * 
	 * 
	 */
	private class Listener extends Thread {
		public Listener() {
			start();
		}

		public void run() {
			try {
				while (true) {
					Object obj = ois.readObject();

					if (obj instanceof ArrayList) {
						obj = (ArrayList<String>) obj;

						// Uppdaterar listan i GUIMess..

						gui.updateCheckBoxes((ArrayList<String>) obj);
						
					} else if (obj instanceof iMessage) {
						printMessage((Message) obj);
					}

				}
			} catch (IOException | ClassNotFoundException e) {
			}
		}
	}

	public static void main(String[] args) {
		new ClientGUI(new ClientController("127.0.0.1", 3520));
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {
			System.err.println("går inte stänga socket");
		}
	}
}
