package rpsServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;

import rpsLibrary.*;

@SuppressWarnings("unused")
public class RPSRoomThread extends Thread{
	private RPSServerState serverState;
	private Socket firstClientSocket;
	private Socket secondClientSocket;
	private BufferedWriter firstClientOut;
	private BufferedWriter secondClientOut;
	private BufferedReader firstClientIn;
	private BufferedReader secondClientIn;
	private int roomNumber;
	private boolean isAccepted;
	private int firstPlayerCurrentMove;
	private int secondPlayerCurrentMove;
	private int winner;
	private boolean isStarted;
	private boolean isPlayerTwoConnected;
	
//	Declarations for constants


	
	public RPSRoomThread(RPSServerState serverState, int roomNumber, Socket firstClientSocket, Socket secondClientSocket) throws IOException {
		super();
		this.serverState = serverState;
		this.roomNumber = roomNumber;
		this.firstClientSocket = firstClientSocket;
		this.secondClientSocket = secondClientSocket;
		
		// Setup readers and writers
		this.firstClientIn = new BufferedReader(new InputStreamReader(firstClientSocket.getInputStream()));
		this.secondClientIn = new BufferedReader(new InputStreamReader(secondClientSocket.getInputStream()));
		
		this.firstClientOut = new BufferedWriter(new OutputStreamWriter(firstClientSocket.getOutputStream()));
		this.secondClientOut = new BufferedWriter(new OutputStreamWriter(secondClientSocket.getOutputStream()));
		
		this.isStarted = false;
		this.isPlayerTwoConnected = true;
	}
	
	public RPSRoomThread(RPSServerState serverState, int roomNumber, Socket firstClientSocket) throws IOException {
		super();
		this.serverState = serverState;
		this.roomNumber = roomNumber;
		this.firstClientSocket = firstClientSocket;
		
		// Setup readers and writers
		this.firstClientIn = new BufferedReader(new InputStreamReader(firstClientSocket.getInputStream()));
		
		this.firstClientOut = new BufferedWriter(new OutputStreamWriter(firstClientSocket.getOutputStream()));
		this.isStarted = false;
		this.isPlayerTwoConnected = false;
	}
	
	public void addSecondPlayer(Socket secondClientSocket) throws IOException
	{
		this.secondClientSocket = secondClientSocket;
		this.secondClientIn = new BufferedReader(new InputStreamReader(secondClientSocket.getInputStream()));
		this.secondClientOut = new BufferedWriter(new OutputStreamWriter(secondClientSocket.getOutputStream()));
		
		this.isPlayerTwoConnected = true;
	}
	
	@Override
	public void run() {
		if(!this.isPlayerTwoConnected)
		{
			throw new IllegalStateException("Player two has not connected yet");
		}
		while(!firstClientSocket.isClosed() && !secondClientSocket.isClosed())
		{
			try {
				this.isStarted = true;
				listenForPlayersMoves();
				System.out.println("This is the input for player 1: " + this.firstPlayerCurrentMove + 
						" player 2: " + this.secondPlayerCurrentMove);
				this.winner = RPSServerProtocol.rockPaperScissorWinner(firstPlayerCurrentMove, secondPlayerCurrentMove);
				if(this.winner == Constants.PLAYER_ONE_WINNER)
				{
					sendMessageToPlayer(Constants.PLAYER_ONE_WON_MESSAGE, Constants.BOTH_PLAYERS);
				}
				else if(this.winner == Constants.PLAYER_TWO_WINNER)
				{
					sendMessageToPlayer(Constants.PLAYER_TWO_WON_MESSAGE, Constants.BOTH_PLAYERS);
				}
				else
				{
					sendMessageToPlayer(Constants.TIE_MESSAGE, Constants.BOTH_PLAYERS);
				}
				
			} 
			catch (SocketException e)
			{
				e.printStackTrace();
				break;
			}
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		}
		System.out.println("One or more of the clients disconnected. Ending the room.");
		try {
			serverState.removeOccupant(roomNumber);
			serverState.removeOccupant(roomNumber);
			serverState.removeRoom(roomNumber);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

	}
	
	private void listenForPlayersMoves() throws IOException, IndexOutOfBoundsException
	{
		// Flushes input before starting to listen again.
		System.out.println("Going to ask for players moves in room " + this.roomNumber);
		this.sendMessageToPlayer(Constants.ASK_FOR_MOVES_MESSAGE, Constants.BOTH_PLAYERS);
		System.out.println("Done! Waiting for a response in room " + this.roomNumber);

		
		String firstPlayerMovesString = firstClientIn.readLine();
		String secondPlayerMovesString = secondClientIn.readLine();
		
		int firstPlayerMoves = RPSServerProtocol.translateMessageToMoves(firstPlayerMovesString);
		int secondPlayerMoves = RPSServerProtocol.translateMessageToMoves(secondPlayerMovesString);
		
		this.firstPlayerCurrentMove = firstPlayerMoves;
		this.secondPlayerCurrentMove = secondPlayerMoves;
	}
	
	private void sendMessageToPlayer(String message, int targetPlayer) throws IndexOutOfBoundsException, IOException
	{
		if(targetPlayer < 0 || targetPlayer > 2)
		{
			throw new IndexOutOfBoundsException("Target Player Input Out of Bounds");
		}
		
		if(targetPlayer == Constants.PLAYER_ONE)
		{
			firstClientOut.write(message);
			firstClientOut.flush();
		}
		
		if(targetPlayer == Constants.PLAYER_TWO)
		{
			secondClientOut.write(message);
			secondClientOut.flush();
		}
		
		if(targetPlayer == Constants.BOTH_PLAYERS)
		{
			firstClientOut.write(message);
			secondClientOut.write(message);
			firstClientOut.flush();
			secondClientOut.flush();
		}
	}
	
}

