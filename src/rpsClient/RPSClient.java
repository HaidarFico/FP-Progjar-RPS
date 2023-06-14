package rpsClient;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import chatClient.ChatClient;

public class RPSClient {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		Scanner sc = new Scanner(System.in);
		String IPAdress = null;
		int portNumber = -1;
		int roomNumber = -1;
		try {
			System.out.println("Please enter the IP Adress");
//			IPAdress = sc.nextLine();
			IPAdress = reader.readLine();
			System.out.println("Please enter the port number");
//			portNumber = sc.nextInt();
			portNumber = Integer.parseInt(reader.readLine());
			System.out.println("Please enter the room number, pick 1-10");
//			roomNumber = sc.nextInt();
			roomNumber = Integer.parseInt(reader.readLine());

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			chatClient.ChatClient chatClient = new ChatClient(reader);
			RPSClientModel Model = new RPSClientModel(IPAdress, portNumber, roomNumber); //Will throw an IOException if there is an error
			System.out.println("Model berhasil di buat");
			RPSCGUI gui = new RPSCGUI(Model);
			System.out.println("Connection succesful to server at IP address " +
			Model.getServerSocket());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
