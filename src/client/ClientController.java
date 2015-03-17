package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import server.UserMessage;

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

	public String getUser() {
		return username;
	}

	public ArrayList<String> getListOnUsers() {
		return usersOnServer;
	}

	public void connect() {
		try {
			socket = new Socket(this.ip, this.port);
			socket.setSoTimeout(999999);
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
		gui = new GUIMess(this);
		listener = new Listener();
	}

	public void sendMessage(String mess, Icon image, ArrayList<String> toUsers) {

		UserMessage newMess = new UserMessage(this.username, mess, image);

		if (toUsers != null) {
			for (int i = 0; i < toUsers.size(); i++) {
				String toUser = toUsers.get(i);
				toUser = toUser.substring(0, toUser.indexOf(':'));
				newMess.setTo(toUser);
				System.out.println(toUser);
			}
			newMess.setTo(username);
		}

		try {
			oos.writeObject(newMess);
			oos.flush();
			System.out.println(newMess.ToUser());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printMessage(UserMessage mess) {
		if (mess instanceof UserMessage) {
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
				try {
					oos.writeObject(username);
					oos.flush();
				} catch (Exception e) {
					System.err.println("Kan inte skicka användarnamn till Server!");
				}
				
				while (true) {

					Object obj = ois.readObject();

					if (obj instanceof ArrayList<?>) {
						obj = (ArrayList<String>) obj;
						// Någon loggar in
						usersOnServer = (ArrayList<String>) obj;
						gui.updateCheckBoxes();
						System.out.println("här kommer jag inte in?");
					}

					if (obj instanceof UserMessage) {
						printMessage((UserMessage) obj);
					}

					if (obj instanceof LinkedList<?>) {
						for (UserMessage message : (LinkedList<UserMessage>) obj) {
							System.out.println(message.getMessage());
							printMessage(message);
						}
					}

				}
			} catch (IOException | ClassNotFoundException e) {
				try {
					ois.close();
					socket.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		new ClientGUI(new ClientController("193.14.131.134", 3520));
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {
			System.err.println("går inte stänga socket");
		}
	}
}
