//package rpsServer;
//
//import java.net.*;
//import java.io.*;
////This class is a class that will contain the individual threads connecting from the server to a client with a socket, it will have a designated player number
////and will have a printwriter for the outputstream and bufferedreader for a inputstream.
////Each thread handles a room
//public class RPSServerThread extends Thread{
//	private RPSServer server;
//	private RPSServerState serverState;
//	private Socket clientSocket;
//	private PrintWriter out;
//	private BufferedReader in;
//	private int playerNumber;
//	private int room_number;
//	private boolean isAccepted;
//
//	//	This method is a constructor, it will get the socket and player number from the main RPSClient class, will throw an IOException if the getOutpuStream or
////	getInputStream method fails
//	public RPSServerThread(Socket Socket, int playerNumber, RPSServer server, RPSServerState serverState) throws IOException {
//		this.clientSocket = Socket;
//		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
//		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		this.playerNumber = playerNumber;
//		this.server = server;
//		this.serverState = serverState;
//		isAccepted = checkForRoomAvailability();
//	}
//	
//	public boolean isAccepted() {
//		return isAccepted;
//	}
//
//	public void setAccepted(boolean isAccepted) {
//		this.isAccepted = isAccepted;
//	}
//
//	public boolean checkForRoomAvailability() throws IOException {
//		String input = in.readLine();
//		int roomNumber = Integer.parseInt(input);
//		this.room_number = roomNumber;
//		return serverState.isRoomAvailable(roomNumber);
//	}
////	This method will get the input from the server, the input is whether this player has one or not
//	public int getRPSAnswer() throws NumberFormatException, IOException {
//		String input = in.readLine(); //Creates a string first to prevent IOExceptions if the other player has not given an input yet
//		return Integer.parseInt(input);
//	}
////	The default getter and setters for the individual attributes
//	public Socket getClientSocket() {
//		return clientSocket;
//	}
//	public void setClientSocket(Socket clientSocket) {
//		this.clientSocket = clientSocket;
//	}
//	public PrintWriter getOut() {
//		return out;
//	}
//	public void setOut(PrintWriter out) {
//		this.out = out;
//	}
//	public BufferedReader getIn() {
//		return in;
//	}
//	public void setIn(BufferedReader in) {
//		this.in = in;
//	}
//	public int getPlayerNumber() {
//		return playerNumber;
//	}
//	public void setPlayerNumber(int playerNumber) {
//		this.playerNumber = playerNumber;
//	}
//	
//	public int getRoom_number() {
//		return room_number;
//	}
//	public void setRoom_number(int room_number) {
//		this.room_number = room_number;
//	}
//	 
//}
