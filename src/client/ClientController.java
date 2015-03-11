package client;

import interfaces.iMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ClientController {
	private String username;
	private LinkedList<String> usersOnline;

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
		// connect();
		// sendUsernameToServer();
		listener = new Listener();

	}

	public void setUser(String username) {
		this.username = username;
		sendUsernameToServer();
	}

	public void sendMessege(String toUser) {

	}

	public void connect() {
		try {
			socket = new Socket(ip, port);
			socket.setSoTimeout(5000);
			ois = new ObjectInputStream(new BufferedInputStream(
					socket.getInputStream()));
			oos = new ObjectOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
		} catch (Exception e) {
			System.err.println("Client kan inte koppla med server!");
		}
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {
			System.err.println("går inte stänga socket");
		}
	}

	public void sendUsernameToServer() {
		try {
			oos.writeObject(username);
			usersOnline = (LinkedList<String>) ois.readObject();
		} catch (Exception e) {
			System.err.println("Kan inte skicka användarnamn till Server!"
					+ "Eller få tillbaka en lista");
		}
	}

	public void sendMessage(String mess, Icon image, String[] toUsers) {
		
		Message newMess = new Message(this.username, mess, image);
		
		newMess.setTo(this.username);
		if (toUsers != null) {
			for (int i = 0; i < toUsers.length; i++) {
				newMess.setTo(toUsers[i]);;
			}
		}
		
		newMess = null;
	}

	public void printMessage(Message mess) {
		if (mess instanceof iMessage) {
			String author, message;
			Icon image;

			author = mess.getUsername();
			message = mess.getMessage();
			image = mess.getImage();

			// gui.writeMess(String author, String mess);
		}
	}

	/**
	 * Tråden lyssnar efter meddelande från server.
	 */
	private class Listener extends Thread {
		public Listener() {
			start();
		}

		public void run() {
			try {
				while (true) {
					printMessage((Message) (ois.readObject()));
					// Väntar på att få ett meddelande från servern
				}
			} catch (IOException | ClassNotFoundException e) {
			}
		}
	}

	public static void main(String[] args) {
		new ClientGUI(new ClientController("localhost", 3550));
	}
}
