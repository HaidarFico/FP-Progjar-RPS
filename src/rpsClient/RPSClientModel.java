package rpsClient;

import java.io.*;
import java.net.*;

import rpsLibrary.Constants;

public class RPSClientModel {
	private Socket serverSocket;
	private BufferedWriter out;
	private BufferedReader in;
	private int move;
	private int roomNumber;
	private boolean isPlayerOne;
	private boolean isReadyToSend;
	
	public BufferedWriter getOut() {
		return out;
	}

	public int getMove() {
		return move;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public boolean isPlayerOne() {
		return isPlayerOne;
	}

	public boolean isReadyToSend() {
		return isReadyToSend;
	}

	public RPSClientModel(String IPAddress, int portNumber, int roomNumber) throws IOException{
		this.serverSocket = new Socket(IPAddress, portNumber);
		this.out = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
		this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		this.roomNumber = roomNumber;
		this.move = Constants.EMPTY_MOVE;
		
		System.out.println("Going to check for room validity");

		checkForRoomValidity();
		
		this.isReadyToSend = false;
	}
	
	private void checkForRoomValidity() throws IOException
	{
		String roomRequest = Constants.ASK_FOR_ROOM_INCOMPLETE.concat(" " + roomNumber + "\n");
		this.out.write(roomRequest);
		this.out.flush();
		System.out.println("Going to check for a response!");
		String response = this.in.readLine();
		System.out.println(response);
		if(response == null)
		{
			throw new IOException("Null Response");
		}
		
		if(Constants.ROOM_FILLED_MESSAGE.contains(response))
		{
			throw new IOException("Room is filled! Please pick another room");
		}
		
		if(Constants.ROOM_EMPTY_MESSAGE.contains(response))
		{
			this.isPlayerOne = true;
			return;
		}
		
		if(Constants.ROOM_ONE_OCCUPANT_MESSAGE.contains(response))
		{
			this.isPlayerOne = false;
			return;
		}
		
		throw new IOException("Response invalid: " + response);
	}
	
	public void sendMove() throws IOException
	{
		String responseMoveString = null;
		if(move == Constants.ROCK)
		{
			responseMoveString = Constants.GIVE_MOVE_INCOMPLETE + Constants.ROCK_STRING + "\n";			
		}
		else if(move == Constants.PAPER)
		{
			responseMoveString = Constants.GIVE_MOVE_INCOMPLETE + Constants.PAPER_STRING + "\n";
		}
		else
		{
			responseMoveString = Constants.GIVE_MOVE_INCOMPLETE + Constants.SCISSOR_STRING + "\n";
		}
		System.out.println("This is what's going to be sent to the server: " + responseMoveString + "ini movenya: " + move);
		out.write(responseMoveString);
		out.flush();
	}
	
	public String listenToServer()
	{
		try {
			String response = in.readLine();
			if(!response.isEmpty())
				return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Constants.DEBUG_SERVER_RESPONSE_INVALID;
		
	}
	
	
	public boolean checkForTie(String serverMessage)
	{
		if(Constants.TIE_MESSAGE.contains(serverMessage))
		{
			return true;
		}
		return false;
	}
	
	
	public boolean checkForWinIfNoTie(String serverMessage)
	{
		// Asumsi sudah tidak mungkin tie.
//		if(serverMessage.equals(Constants.PLAYER_ONE_WON_MESSAGE))

		if(Constants.PLAYER_ONE_WON_MESSAGE.contains(serverMessage))
		{
			if(this.isPlayerOne)
			{
				return true;
			}
			return false;
		}
		if(Constants.PLAYER_TWO_WON_MESSAGE.contains(serverMessage))
		{
			if(this.isPlayerOne)
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void setMovePaper()
	{
		this.move = Constants.PAPER;
	}
	
	public void setMoveRock()
	{
		this.move = Constants.ROCK;
	}
	
	public void setMoveScissor()
	{
		this.move = Constants.SCISSOR;
	}
	
//	This is a legacy version of the outputRPS method, this uses a command line interface, therefore is not used for this current GUI implementation.
//	However, this method is still useful for debugging purposes
//	public void outputRPSLegacy() throws IOException {
//		System.out.println("What will you choose? 1. rock 2. paper 3. scissor");
//		Scanner sc = new Scanner(System.in);
//		while(sc.hasNextLine()) {
//			out.println(sc.nextLine());
//			String winnerString = in.readLine();
//			int winner = Integer.parseInt(winnerString);
//			if(winner == 0) 
//				System.out.println("It's a draw");
//			else if(winner == 1) 
//				System.out.println("You win");
//			else 
//				System.out.println("You lose");
//		}
//		sc.close();
//	}
////	Three specific methods to set the answer attribute, this method is created to simplify the setting of the answer variable.
//	public void setAnswerRPSRock() {
//		answer = 1;
//		System.out.println("outputRPSROCK ran");
//	}
//	
//	public void setAnswerRPSPaper(){
//		answer = 2;
//		System.out.println("outputRPSPaper ran");
//	}
//	
//	public void setAnswerRPSScissors(){
//		answer = 3;
//		System.out.println("outputRPSScissors ran");
//	}
//// This method is the method that will broadcast the answer to the server, then listens for the status of the winnings. The SocketException is also used
////	To show if the client can no longer connect to the server.
//	public int checkWin() throws IOException, InvalidParameterException, SocketException{
//		out.flush(); //clears the outputstream
//		out.println(move);
//		this.move = 10; //resets the answer variable
//		String response;
//		while((response = in.readLine()) != null) {
//			int result = Integer.parseInt(response); //This result variable is the status of whether the client has won, lost, or drawn.
//			if(result > -2 && result < 2) 
//				return result;
//			break;
//		}
//		throw new InvalidParameterException(); //Will throw an InvalidParamaterException if the response from the server does not meet the range.
//	}
//	Standard getters and setters for each attribute
	public Socket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}
}
