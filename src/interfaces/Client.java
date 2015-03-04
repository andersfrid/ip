package interfaces;

import java.io.IOException;

import client.ClientController;

public interface Client {
	public void setClientController(ClientController clientController);
	public void put(String name) throws IOException;
	public void get(String name) throws IOException;
	public void list() throws IOException;
	public void remove(String name) throws IOException;
	public void exit() throws IOException;
}

