package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import interfaces.Client;

public class ClientUser implements Client {
	private ClientController controller;
	private String ip;
	private int port;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	

	//Hämtar vilket ip och vilken port vi ska connecta på.
	public ClientUser(String ip, int port){
		this.ip = ip;
		this.port = port;
		
	}

	public void setClientController(ClientController controller) {
		this.controller = controller;
		
	}
	//skapar kontakt med server
	public void connect() throws IOException{
		socket = new Socket(ip,port);
		socket.setSoTimeout(5000);//om ingen kontakt till server hittas timeout.
		dis = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // en ström så vi kan lägga saker på server?
		dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())); // en ström så vi kan hämta saker!
	}
	
	// stänger ner anslutning till servern.
	public void disconnect() throws IOException{
		socket.close();	
	}
	
	//Skriver ner värde på servern
		public void put(String text) throws IOException {
		connect();
		dos.writeUTF(text); //vad vi vill skicka till servern
		dos.flush(); //skickar iväg det
		String mess = dis.readUTF(); //skickar det vi skrev till textrutan
		controller.newResponse(mess); // skickar det vi skrev till metod i controller
//		disconnect();
			
		}

	//Hämtar värde som finns på servern!!
	public void list() throws IOException {
		connect();
		
		
	}

	//Tar bort värde som finns på servern!!
	public void remove(String text) throws IOException {
	
		
	}

	
	public void exit() throws IOException {
	
		
	}
	
	public void get(String text) throws IOException {
		
		
	}
}
