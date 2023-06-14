package rpsServer;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.util.Scanner;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
//The main class of the rpsServer package, includes the main method that will start a new server with a server socket with a port number that is user input.
//Each player will be assigned to a server thread, will also start the rock paper scissors game

public class RPSServer{
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Please enter the port number that you wish to use:");
		Scanner sc = new Scanner(System.in);
		int portNumber = sc.nextInt();
		try {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			serverSocket.close();
		}
		catch(BindException e) {
			System.err.println("Sorry, there is a program already using that port number!");
			TimeUnit.SECONDS.sleep(5);
			System.exit(1);
		}
//		ServerSocket serverSocket = new ServerSocket(portNumber);
		ServerSocket serverSocket = new ServerSocket(portNumber, 20);
		RPSServerState serverState = new RPSServerState();
		Socket clientSocket = null;
		boolean loopStatus = true;		
		sc.close();
		
		try {
			chatServer.ChatServer chatServer = new chatServer.ChatServer();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		while(loopStatus) {
			try 
			{
				System.out.println("Listening to requests...");				
				clientSocket = serverSocket.accept();
				System.out.println("A user with the ip of " + clientSocket.getInetAddress() + " has connected!");
				RPSRoomThread roomThread = RPSServerProtocol.acceptClientHandler(serverState, clientSocket);
				if(roomThread != null)
				{
					roomThread.start();
				}
			}
			catch(IOException e) {
				System.err.println("A user has disconnected, waiting 5 seconds then restarting the server...");
			}
			catch(NumberFormatException e1) {
				System.err.println("Incorrect number input");
				e1.printStackTrace();
				System.exit(1);
			}	
			catch(IllegalArgumentException e2) {
				System.err.println("An illegal argument has been entered in the serverOutput method"); 
				e2.printStackTrace();
				System.exit(1);
			}
			catch(TooManyListenersException e3)
			{
				System.err.println("The room is filled");
				clientSocket.close();
				e3.printStackTrace();
			}
			catch(InvalidKeyException e4)
			{
				System.err.println("The room is filled");
				clientSocket.close();
				e4.printStackTrace();
			}
		}
			sc.close();
			serverSocket.close();
		}
	}

