package rpsServer;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import rpsLibrary.*;

public class RPSServerState {

	
	private int[] RoomOccupation = new int[10];
	private ArrayList<RPSRoomThread> roomList = new ArrayList<RPSRoomThread>();
	private int roomNumber;

	public RPSServerState() 
	{
		for(int i = 0; i < 10; i++)
		{
			RoomOccupation[i] = Constants.ZERO_OCCUPANTS;
			roomList.add(null);
		}
	}
	
	public void removeRoom(int roomNumber)
	{
		this.roomNumber = roomNumber;
		if(roomList.get(roomNumber - 1) == null)
		{
			throw new IndexOutOfBoundsException("Index is out of bounds. The room list is already empty.");
		}
		
		roomList.set(roomNumber - 1, null);
	}
	public void addRoom(int roomNumber, RPSRoomThread roomThread) throws IndexOutOfBoundsException
	{
		if(roomList.size() > 10)
		{
			throw new IndexOutOfBoundsException("Room is full");
		}
		
		if(roomList.get(roomNumber - 1) != null)
		{
			throw new IndexOutOfBoundsException("Room is already used!");
		}
		
		roomList.set(roomNumber - 1, roomThread);
	}
	
	public boolean isRoomAvailable(int roomNumber) {
		boolean result = false;
		if (roomNumber >= 1 && roomNumber <= 10) {
			if (this.getRoomOccupation()[roomNumber - 1] <= 1) {
				result = true;
			}
		}
		return result;
	}
	
	public void addOccupant(int roomNumber) throws InvalidKeyException
	{
		this.roomNumber = roomNumber;
		if(this.RoomOccupation[roomNumber - 1] > 1)
		{
			throw new InvalidKeyException("Room Occupants is full");
		}
		this.RoomOccupation[roomNumber - 1] = this.RoomOccupation[roomNumber - 1] + 1;
	}
	
	public void removeOccupant(int roomNumber) throws InvalidKeyException
	{
		this.roomNumber = roomNumber;
		if(this.RoomOccupation[roomNumber - 1] < 1)
		{
			throw new InvalidKeyException("Room Occupants is empty");
		}
		this.RoomOccupation[roomNumber - 1] = this.RoomOccupation[roomNumber - 1] - 1;
	}
	
	public int[] getRoomOccupation() {
		return RoomOccupation;
	}

	public void setRoomOccupation(int[] roomOccupation) {
		RoomOccupation = roomOccupation;
	}
	
	public ArrayList<RPSRoomThread> getRoomList() {
		return roomList;
	}

	public void setRoomList(ArrayList<RPSRoomThread> roomList) {
		this.roomList = roomList;
	}
	
}
