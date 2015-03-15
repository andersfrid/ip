package server;

import interfaces.iMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.ClientController;
import client.ClientGUI;
import client.Message;

public class Server {
	private int port;
	private ArrayList<String> list = new ArrayList<String>();

	public Server(int port) {
		for (int i = 0; i < 5; i++) {
			list.add("din mamma " + i);
		}
		
		this.port = port;
		new Connection(port).start();
	}

	
	/**
	 * En tråd ligger och väntar på den ska få in en användare.
	 * När den får en anävndare skapas en en seperat tråd som tar hand om just den anävndaren och lyssnar.
	 * @author Andreas
	 *
	 */
	private class Connection extends Thread {
		private int port;

		public Connection(int port) {
			this.port = port;
		}

		public void run() {
			Socket socket = null;
			System.out.println("Server startad");
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (true) {
					try {
						socket = serverSocket.accept();
						new ClientHandler(socket); //Kanske måste ändra senare
					} catch (IOException e) {
						System.err.println(e);
						if (socket != null)
							socket.close();
					}
				}
			} catch (IOException e) {
				System.err.println(e);
			}
			System.out.println("Server stoppad");
		}
	}

	
	/**
	 * 
	 * Tar hand om en användare i systemet
	 * @author Andreas
	 *
	 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			start();
		}

		public void run() {
			try{
				while(!interrupted()){
					Object obj = null;
					obj = ois.readObject();
					
					if(obj instanceof String){
						obj = (String)obj;
						System.out.println(obj);
						list.add((String)obj);
						oos.writeObject(list);
					}
					
					else if(obj instanceof iMessage){
						obj = (iMessage)obj;
						System.out.println("Meddelande från: "+ ((iMessage) obj).getUsername()+ " som skriver: "+((iMessage) obj).getMessage()+" Och det är till: "+((iMessage) obj).ToUser());
					}
				}
			}catch(Exception e){}
			System.out.println("Klient nerkopplad");
		}
	}
	
	public static void main(String[] args) {
		new Server(3520);
		new ClientGUI(new ClientController("127.0.0.1", 3520));
	}
}
