package chatClient;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 9000;
	private Scanner sc;
	private BufferedReader reader;

	public ChatClient(BufferedReader reader){
		this.reader = reader;

       
		ChatThread chatThread = new ChatThread(sc, reader);
		chatThread.start();

	}

private class ChatThread extends Thread{
	private Scanner scanner;
	private BufferedReader reader;
	
	public ChatThread(Scanner scanner, BufferedReader reader) {
		this.scanner = scanner;
		this.reader = reader;
	}
	@Override
	public void run() {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			System.out.print("Tolong Masukkan Username Anda: ");
			String username = reader.readLine();

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			oos.writeObject(username);
			oos.flush();

			Thread inputHandler = new InputHandler(reader, oos, username);
			inputHandler.start();

			messages.Messages message;
	            // add welcome and list of menu
			System.out.println("Welcome to the chatroom, " + username + "!");
			System.out.println("List of commands: ");
			System.out.println("users - to see online users");
			System.out.println("private <username> <message> - to send message to a user");
			System.out.println("quit - to quit the chatroom");

			while ((message = (messages.Messages) ois.readObject()) != null
	                    && !message.getSender().equals("bye")) {
	                if (message.getMessageContent().equals("bye")) {
	                    break;
	                } else if (message.getSender().equals("server") && !message.getMessageContent().equals("bye") && message.isPrivate()) {
	                    System.out.println("Online users: \n" + message.getMessageContent());
	                } else {
	                    System.out.println(message.getSender() + ": " + message.getMessageContent());
	                }
	            }

	            ois.close();
	            oos.close();
//	            reader.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}
	
