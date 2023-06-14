package rpsLibrary;

public class Constants {
	public static final int ROCK = 11;
	public static final int PAPER = 22;
	public static final int SCISSOR = 33;
	public static final String ROCK_STRING = "Rock";
	public static final String PAPER_STRING = "Paper";
	public static final String SCISSOR_STRING = "Scissor";
	public static final int EMPTY_MOVE = 44;
	
	public static final String START_GAME_MESSAGE = "Server: Game Start\n";
	public static final String ASK_FOR_MOVES_MESSAGE = "Server: Moves\n";
	public static final String PLAYER_ONE_WON_MESSAGE = "Server: P1 Wins\n";
	public static final String PLAYER_TWO_WON_MESSAGE = "Server: P2 Wins\n";
	public static final String TIE_MESSAGE = "Server: Tie\n";
	public static final String ROOM_FILLED_MESSAGE = "Server: The room is filled\n";
	public static final String ROOM_EMPTY_MESSAGE = "Server: The room is empty\n";
	public static final String ROOM_ONE_OCCUPANT_MESSAGE = "Server: The room has one occupant\n";
	public static final String MESSAGE_FROM_CLIENT_IS_INVALID = "Server: The message is invalid\n";
	
	public static final String ASK_FOR_ROOM_INCOMPLETE = "Player: Want Room";
	public static final String GIVE_MOVE_INCOMPLETE = "Player: Move ";
	
	
	public static final int ZERO_OCCUPANTS = 0;
	public static final int ONE_OCCUPANTS = 1;
	public static final int TWO_OCCUPANTS = 2;
	
	public static final int PLAYER_ONE_WINNER = 0;
	public static final int PLAYER_TWO_WINNER = 1;
	public static final int TIE = 2;
	public static final int PLAYER_ONE = 0;
	public static final int PLAYER_TWO = 1;
	public static final int BOTH_PLAYERS = 2;
	
	public static final String DEBUG_SERVER_RESPONSE_INVALID = "Invalid response from server";
}
