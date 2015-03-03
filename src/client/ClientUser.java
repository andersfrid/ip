package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientUser {
	private ClientController controller;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientUser(String ip, int port) throws IOException{
		this.socket = new Socket(ip,port);
		ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
}
