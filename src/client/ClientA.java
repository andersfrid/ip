package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Interfaces.Client;

public class ClientA implements Client {
	private ClientController controller;
	private Socket socket;
	private ObjectInputStream dis;
	private ObjectOutputStream dos;
	
	public ClientA(String ip, int port) throws IOException {
		socket = new Socket(ip,port);
		dis = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		dos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		new Listener().start();
	}
	
	public void setClientController(ClientController controller) {
		this.controller = controller;
	}

	public void put(String name) throws IOException {
		dos.writeUTF("");
		dos.writeUTF(name);
		dos.flush();
	}

	public void get(String name) throws IOException {
		dos.writeUTF("");
		dos.writeUTF(name);
		dos.flush();
	}

	public void list() throws IOException {
		dos.writeUTF("LIST");
		dos.flush();
	}

	public void remove(String name) throws IOException {
		dos.writeUTF("REMOVE");
		dos.writeUTF(name);
		dos.flush();
	}

	public void exit() throws IOException {
		if(socket!=null)
		    socket.close();		
	}

	private class Listener extends Thread {
		public void run() {
			String response;
			try {
				while(true) {
					response = dis.readUTF();
					controller.newResponse(response);
				}
			} catch(IOException e) {}
			try {
				exit();
			} catch(IOException e) {}
			controller.newResponse("Klient kopplar ner");
		}
	}
}

