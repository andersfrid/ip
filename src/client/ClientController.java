package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

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

	public ClientController(String ip, int port) {
		this.ip = ip;
		this.port = port;
		
		//connect();
		//sendUsernameToServer();
		
	}
	
	public void setUser(String username){
		this.username = username;
		System.out.println(username);
	}
	
	public void sendMessege(String toUser){
		
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
			usersOnline = (LinkedList<String>)ois.readObject();
		} catch (Exception e) {
			System.err.println("Kan inte skicka användarnamn till Server!");
		}
	}

	public static void main(String[] args) {
		new ClientGUI(new ClientController("localhost", 3550));
	}
}
