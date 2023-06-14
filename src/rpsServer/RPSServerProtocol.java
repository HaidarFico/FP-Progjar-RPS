package rpsServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.TooManyListenersException;

import rpsLibrary.Constants;

public class RPSServerProtocol {
	
//	static int rockPaperScissorWinnerLegacy(int a, int b) {
//		if((a > 0 || a < 4) || (b > 0 || b < 4)) { 
//			if(a==1) {				//rock
//				  if(b==3)
//					  return 1;
//				  else if (b==2)
//					  return -1;
//				  else
//					  return 0;
//				  }
//			else if(a==2) {				//paper
//				  if(b==3) 
//					  return -1;
//				  else if (b==2)
//					  return 0;
//				  else
//					  return 1;
//			}
//			else if(a==3) {			//scissor
//				  if(b==3)
//					  return 0;
//				  else if (b==2) 
//					  return 1;	 
//				  else{
//					  return -1;
//			}
//		}
////			These lines of codes will compare and return the winners of the rock paper scissor game
//	}
//	return -2; //-2 is the code for an error
//}

	static int rockPaperScissorWinner(int firstPlayerMove, int secondPlayerMove) {
		//Asumsi input selalu valid.
		
		if(firstPlayerMove == secondPlayerMove)
		{
			return Constants.TIE;
		}
		if(firstPlayerMove == Constants.SCISSOR)
		{
			if(secondPlayerMove == Constants.PAPER)
			{
				return Constants.PLAYER_ONE_WINNER;
			}
			return Constants.PLAYER_TWO_WINNER;
		}
		if(firstPlayerMove == Constants.PAPER)
		{
			if(secondPlayerMove == Constants.ROCK)
			{
				return Constants.PLAYER_ONE_WINNER;
			}
			return Constants.PLAYER_TWO_WINNER;
		}
		
		// firstPlayerMove pasti ROCK
		if(secondPlayerMove == Constants.SCISSOR)
		{
			return Constants.PLAYER_ONE_WINNER;
		}
		return Constants.PLAYER_TWO_WINNER;
	}

// Legacy Function
//	static void serverOutput(int winner, RPSServerThread playerOne, RPSServerThread playerTwo) throws IllegalArgumentException {
//		if(winner == 0) {
//			System.out.println("The result is a draw");
//			playerOne.getOut().println(0);
//			playerTwo.getOut().println(0);
//		}
//		else if(winner == 1) {
//			System.out.println("Player one wins");
//			playerOne.getOut().println(1);
//			playerTwo.getOut().println(-1);
//		}
//		else if(winner == -1) {
//			System.out.println("Player two wins");
//			playerOne.getOut().println(-1);
//			playerTwo.getOut().println(1);
//		}
//		else {
//			throw new IllegalArgumentException();
//		}
//	}
	
//	Legacy Function
//	static void startGame(RPSServerThread playerOne, RPSServerThread playerTwo) throws NumberFormatException, IOException, IllegalArgumentException {
//		int winner = rockPaperScissorWinner(playerOne.getRPSAnswer(), playerTwo.getRPSAnswer());
//		serverOutput(winner, playerOne, playerTwo);
//	}
	
	static int translateMessageToMoves(String message) throws IndexOutOfBoundsException
	{
		System.out.println("ini message yang mau ditranslate ke move" + message);
		if(message.contains(Constants.SCISSOR_STRING))
		{
			return Constants.SCISSOR;
		}
		
		if(message.contains(Constants.ROCK_STRING))
		{
			return Constants.ROCK;
		}
		
		if(message.contains(Constants.PAPER_STRING))
		{
			return Constants.PAPER;
		}
		throw new IndexOutOfBoundsException("Message sent by the client does not translate to any moves.");
	}
	
	static RPSRoomThread acceptClientHandler(RPSServerState serverState, Socket clientSocket) throws IOException, IndexOutOfBoundsException, TooManyListenersException, InvalidKeyException
	{
		int clientRoomNumber = -1;
		boolean isMessageValid = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		String roomMessage = in.readLine();
		System.out.println(roomMessage);
		for(int i = 1; i <= 10; i++)
		{
			if(roomMessage.contains(Integer.toString(i)))
			{
				clientRoomNumber = i;
				isMessageValid = true;
				break;
			}
		}
		
		if(!isMessageValid)
		{
			out.write(Constants.MESSAGE_FROM_CLIENT_IS_INVALID);
			out.flush();
			in.close();
			out.close();
			throw new IndexOutOfBoundsException("The message received's room number is more than 10 or less than 1");
		}
		
		if(!serverState.isRoomAvailable(clientRoomNumber))
		{
			out.write(Constants.ROOM_FILLED_MESSAGE);
			out.flush();
			in.close();
			out.close();
			throw new TooManyListenersException("The selected room is filled");
		}
		
		if(serverState.getRoomList().get(clientRoomNumber - 1) == null)
		{
			RPSRoomThread thread = new RPSRoomThread(serverState, clientRoomNumber, clientSocket);
			serverState.addOccupant(clientRoomNumber);
			serverState.addRoom(clientRoomNumber, thread);
			
			out.write(Constants.ROOM_EMPTY_MESSAGE);
			out.flush();

			return null;
		}
		
		RPSRoomThread onePlayerRoomThread = serverState.getRoomList().get(clientRoomNumber - 1);
		onePlayerRoomThread.addSecondPlayer(clientSocket);
		serverState.addOccupant(clientRoomNumber);
		
		out.write(Constants.ROOM_ONE_OCCUPANT_MESSAGE);
		out.flush();

//		onePlayerRoomThread.run();
		return onePlayerRoomThread;
	}
	
}
