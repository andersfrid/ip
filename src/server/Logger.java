package server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logger {
	/**
	 * Saves the information to the log file.
	 * 
	 * @param logMessage
	 *            The message to put in the file.
	 * @throws IOException
	 */
	private void log(String logMessage) throws IOException {
		File file = new File("log.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));

		synchronized (out) {
			out.println(logMessage);
			out.flush();
			out.close();
		}
	}

	public void logError(String username) throws IOException {
		log("Could not find user " + username);
	}
	/**
	 * Logs a users login.
	 * 
	 * @param username
	 * @throws IOException
	 */
	public void logUser(String username) throws IOException {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		log(date +" "+ username + " logged in");
	}

	/**
	 * Logs sent information.
	 * 
	 * @param username
	 *            The username of the user that sent the message.
	 * @param toUser
	 *            The username of the user to recieve the message.
	 * @param message
	 *            The message sent.
	 * @throws IOException
	 */
	public void logMessage(String username, String toUser, String message)
			throws IOException {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		
		if (toUser.isEmpty()) {
			log(date + ":" + username + " wrote " + message);
		} else {
			log(date + ":" + username + " wrote to " + toUser + ":"
					+ message);
		}
	}

	/**
	 * Saves a message sent to all users.
	 * 
	 * @param userMessage
	 *            The message to be saved.
	 * @throws IOException
	 */
	public void saveAllMessage(UserMessage userMessage) throws IOException {
		File dir = new File("Messages");

		if (!dir.exists()) {
			dir.mkdir();
		}

		File[] directoryListing = dir.listFiles();

		if (directoryListing != null) {
			for (File child : directoryListing) {
				saveToUserMessage(userMessage, child);
			}
		}
		
		logMessage(userMessage.getUsername(), "", userMessage.getMessage());
	}

	/**
	 * Saves a message sent to a user, in the designated file.
	 * 
	 * @param userMessage
	 *            The message to be saved
	 * @param file
	 *            The file to save to.
	 * @throws FileNotFoundException
	 */
	private void saveToUserMessage(UserMessage userMessage, File file)
			throws FileNotFoundException {
		PrintWriter out = new PrintWriter(file);

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		
		synchronized (out) {
			out.println(date + ";" + userMessage.getUsername()
					+ "; wrote; " + userMessage.getMessage());
			out.flush();
			out.close();
		}
	}

	/**
	 * Saves a message that was sent to a specific user.
	 * 
	 * @param toUser
	 *            The user to recieve the message.
	 * @param userMessage
	 *            The message sent.
	 * @throws IOException
	 */
	public void saveToMessage(String toUser, UserMessage userMessage)
			throws IOException {
		File yourFile = new File("Messages/" + toUser + ".txt");
		if (!yourFile.exists()) {
			yourFile.createNewFile();
		}

		saveToUserMessage(userMessage, yourFile);
	}

	/**
	 * Gets ten previous messages sent to the user.
	 * 
	 * @param username
	 *            The username of the user.
	 * @return ArrayList<String> with the messages.
	 * @throws IOException
	 */
	public ArrayList<String> checkMessages(String username) throws IOException {
		ArrayList<String> messages = new ArrayList<String>();
		File file = new File("Messages/" + username + ".txt");

		if (file.exists()) {
			int count = 0;

			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);

			while (bReader.read() != -1) {
				String s = bReader.readLine();
				messages.add(s);
				count++;

				if (count > 10) {
					break;
				}
			}
		}

		return messages;
	}
}
