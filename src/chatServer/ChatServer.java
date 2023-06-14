package chatServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.*;

public class ChatServer {
    private static final int PORT = 9000;
    private static List<ClientHandler> clients = new ArrayList<>();
    
    public ChatServer(){
    	ServerThread serverThread = new ServerThread();
    	serverThread.start();
    }
    
    private class ServerThread extends Thread{

		@Override
		public void run() {
			super.run();
			   try {
		        	ServerSocket serverSocket = new ServerSocket(PORT);
		            System.out.println("Server mulai dalam port: " + PORT);

		            while (true) {
		                Socket clientSocket = serverSocket.accept();
		                System.out.println("New client connected");

		                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
		                synchronized (clients) {
		                    clients.add(clientHandler);
		                    clientHandler.start();
		                }
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		}
    	
    }
}