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
		System.out.println("1");
		this.ip = ip;
		this.port = port;
		System.out.println("2");
		connect();

		// listener = new Listener();
	}

	public void setUser(String username) {
		this.username = username;
	}

	public ArrayList<String> getListOnUsers() {
		return usersOnServer;
	}

	public void connect() {
		try {
			System.out.println("3");
			socket = new Socket(this.ip, this.port);
			socket.setSoTimeout(5000);
			System.out.println("4");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("5");
			System.out.println("kan koppla med server");
		} catch (Exception e) {
			System.err.println("Client kan inte koppla med server!");
			System.out.println(e);
		}
	}

	public void startGUIMess() {
		try {
			oos.writeObject(new Login(this.username));
			oos.flush();

		} catch (Exception e) {
			System.err.println("Kan inte skicka användarnamn till Server!");
		}
		try {
			Object obj = ois.readObject();

			if (obj instanceof ArrayList) {
				usersOnServer = (ArrayList<String>) obj;
				System.out.println("Får något!");
				usersOnServer.toString();
			}

		} catch (Exception e) {
			System.err
					.println("Kan inte få tillbaka en lista");
		}
	}

	public void sendMessage(String mess, Icon image, String[] toUsers) {

		Message newMess = new Message(this.username, mess, image);

		if (toUsers != null) {
			for (int i = 0; i < toUsers.length; i++) {
				newMess.setTo(toUsers[i]);
			}
		}

	}

	public void printMessage(Message mess) {
		if (mess instanceof iMessage) {
			String author, message;
			Icon image;

			author = mess.getUsername();
			message = mess.getMessage();
			image = mess.getImage();

			gui.writeMessage(author, message);
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
