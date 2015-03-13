package client;

import interfaces.Login;
import interfaces.iMessage;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
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
			socket.setSoTimeout(70000);
			System.out.println(socket.toString());
			System.out.println("4");
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("5");
			System.out.println("kan koppla med server");
		} catch (Exception e) {
			System.err.println("Client kan inte koppla med server!");
			System.out.println(e);
		}
	}


	public void startGUIMess() {
		try {
			while(socket == null)
			{
				Thread.sleep(1000);
				connect();
			}
			
			oos.writeObject(new Login(this.username));
			oos.flush();

		} catch (Exception e) {
			System.err.println("Kan inte skicka användarnamn till Server!");
		}
		
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while(((bytesRead = ois.read(buffer)) != -1))
			{
				String s = new String(buffer);
				System.out.println(s);
			}
			
			
//			ArrayList<String> obj = (ArrayList<String>)ois.readObject();
//			System.out.println("hej");
//			
//			if (obj instanceof ArrayList) {
////				usersOnServer = (ArrayList<String>)obj;
//				System.out.println("Får något!");
//				usersOnServer.toString();
//			}
		} 
		catch(SocketTimeoutException e)
		{
			connect();
		}
		catch (Exception e) {
			System.err.println("Kan inte få tillbaka en lista");
			System.err.println(e);
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
