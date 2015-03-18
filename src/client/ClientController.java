package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.Icon;
import server.UserMessage;
/**
 * Klass som hanterar logik mellan klient och server.
 * @author Andreas Appelqvist, David Beer, Joakim Gustavsson, Anders Frid
 *
 */
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
/**
 * Kontruktor som tar emot ip och port, samt startar sockets och strömmar.
 * @param ip
 * @param port
 */
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
/**
 * Metod som öppnar socket med given ip och port, samt skapar strömmar.
 */
	public void connect() {
		try {
			socket = new Socket(this.ip, this.port);
			socket.setSoTimeout(999999);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Klient kopplar med server");
		} catch (Exception e) {
			System.err.println("Klient kan inte koppla med server!");
			System.out.println(e);
		}
	}
//Anropas från ClientGUI och startar listener
	public void startGUIMess() {
		gui = new GUIMess(this);
		listener = new Listener();
	}
/**
 * Bygger en usermessage och skickar till servern. 
 * @param mess Tar in ett meddelande som en sträng.
 * @param image En bild som ett icon.
 * @param toUsers En string array med specifika användare.
 */
	public void sendMessage(String mess, Icon image, ArrayList<String> toUsers) {

		UserMessage newMess = new UserMessage(this.username, mess, image);

		if (toUsers != null) {
			for (int i = 0; i < toUsers.size(); i++) {
				String toUser = toUsers.get(i);
				toUser = toUser.substring(0, toUser.indexOf(':'));
				newMess.setTo(toUser);
			}
			newMess.setTo(username);
		}

		try {
			oos.writeObject(newMess);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Kollar om meddelandet är meddelandet är en instans av usermessage. sätter autor, meddelandet och bilden.
	 * @param mess tar in meddelandet.
	 */
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
	 * Startar en tråd som skickar iväg användarnamnet till servern. Ligger sen och väntar på få ett
	 * meddelande från servern. Sen kollar den vad den får av servern, Om vi får tillbaka en arraylist så är
	 * en lista på folk online. Får vi ett usermessage så är det ett meddelande. Om det är en linked list vi får
	 * får vi in de gamla meddelandena från servern.
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
						
						usersOnServer = (ArrayList<String>) obj;
						gui.updateCheckBoxes();
					}

					if (obj instanceof UserMessage) {
						printMessage((UserMessage) obj);
					}

					if (obj instanceof LinkedList<?>) {
						for (UserMessage message : (LinkedList<UserMessage>) obj) {
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
	//En metod för att stänga socket.
	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {
			System.err.println("går inte stänga socket");
		}
	}
	// Main metoden som startar clienten, Här skriver du i ip adressen som han som startat servern har.
	public static void main(String[] args) {
		new ClientGUI(new ClientController("127.0.0.1", 3520));
	}

}
