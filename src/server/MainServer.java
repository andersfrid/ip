package server;

import interfaces.iMessage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

import client.ClientController;
import client.ClientGUI;

public class MainServer {
	private ServerSocket connSocket;
	private int outPort = 3520;
	private ClientListener listener = new ClientListener();
	private UserList userList = new UserList();

	public MainServer() throws IOException {
		connSocket = new ServerSocket(3520, 0, InetAddress.getByName(null));

		listener.start();
	}

	public static void main(String[] args) throws IOException {
		new MainServer();
		new ClientGUI(new ClientController("127.0.0.1", 3520));
	}

	private class MessageHandler extends Thread {
		private UserMessage userMessage;
		private Logger logger = new Logger();

		public MessageHandler(iMessage message) {
			userMessage = (UserMessage) message;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				if (userMessage.ToUser().isEmpty()) {
					try {
						sendToAll();
						logger.saveAllMessage(userMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (!userMessage.ToUser().isEmpty()) {
					LinkedList<String> listOfUsers = userMessage.ToUser();

					for (String user : listOfUsers) {
						try {
							User u = userList.findUser(user);

							if (u != null && u.OutStream != null) {

								sendToSpecific(u, userMessage);

								logger.saveToMessage(u.Username, userMessage);
							}
							else {
								logger.saveToMessage(u.Username,userMessage);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				interrupt();
			}
		}

		private void sendToSpecific(User to, UserMessage message)
				throws IOException {
			synchronized (to.OutStream) {
				to.OutStream.writeObject(message);
				to.OutStream.flush();
			}

			logger.logMessage(userMessage.getUsername(), to.Username,
					userMessage.getMessage());
		}

		private void sendToAll() throws IOException {
			for (User u : userList) {
				synchronized (u.OutStream) {
					u.OutStream.writeObject(userMessage);
					u.OutStream.flush();
				}

				logger.logMessage(userMessage.getUsername(), null,
						userMessage.getMessage());
			}
		}
	}

	private class ClientListener extends Thread {
		private Socket socket;

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					System.out.println("startar asdadasdasd");
					socket = connSocket.accept();

					ObjectInputStream inStream = new ObjectInputStream(
							socket.getInputStream());

					Object obj = inStream.readUTF();
					System.out.println(obj);

					if (obj instanceof String) {
						User newUser = new User((String) obj,socket.getInetAddress(),socket.getOutputStream());

						if(!userList.exists(newUser))
						{
							userList.add(newUser);
						}
						else 
						{
							//TODO Check so user isn't online already, to counter "username stealing".
							userList.updateUser(newUser);
						}
						
						updateClientLists(userList.getUserList(),newUser);
					} else if (obj instanceof iMessage) {
						MessageHandler handler = new MessageHandler(
								(iMessage) obj);
						handler.start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void updateClientLists(ArrayList<String> list,User user) throws IOException {
			synchronized (user.OutStream) {
				user.OutStream.writeObject(list);
				user.OutStream.flush();
			}
		}
	}
}
