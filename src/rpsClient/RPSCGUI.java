package rpsClient;
import javax.swing.*;

import rpsLibrary.Constants;

import java.awt.event.*;
import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;
import java.awt.*;

public class RPSCGUI{
	public RPSCGUI(RPSClientModel Model) {
		//create a new frame
		JFrame frame = new JFrame();
		frame.setBounds(10,10,1000,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Rock Paper Scissor Lite");  
		//set frame ui layout
		frame.setLayout(new BorderLayout()); 

		
		//this is the textbox to show information about the game and the server conditions
		JTextArea tf = new JTextArea(); 
		tf.setBounds(10,10,400,200);
		tf.setAlignmentX(50);
		tf.setText("Welcome!");
		
		//create each JButton and it's ActionListener function
		// we create rock,paper,scissor button for the player to choose from.
		// and the outputToServer button to pass the information to the server
		JButton rock = new JButton(new ImageIcon(getClass().getResource("rock.jpg")));

		rock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				if(Model.isReadyToSend())
//				{
					tf.setText("You pressed rock");
					Model.setMoveRock();			
//				}
//				else {
//					tf.setText("Not your turn");
//				}

			}
		});
		JButton paper = new JButton(new ImageIcon(getClass().getResource("paper.jpg")));

		paper.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				if(Model.isReadyToSend())
//				{
					tf.setText("You pressed paper");
					Model.setMovePaper();			
//				}
//				else {
//					tf.setText("Not your turn");
//				}
			}
		});
		
		JButton scissor = new JButton(new ImageIcon(getClass().getResource("scissor.jpg")));

		scissor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				if(Model.isReadyToSend())
//				{
					tf.setText("You pressed scissor");
					Model.setMoveScissor();			
//				}
//				else {
//					tf.setText("Not your turn");
//				}
			}
		});
		
		JButton outputToServer = new JButton(new ImageIcon(getClass().getResource("GOBUTTON.jpg")));
		outputToServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("Gonna read the server's ask for moves.");
					String response = Model.getIn().readLine();
					System.out.println(response);
					if(response == null || !Constants.ASK_FOR_MOVES_MESSAGE.contains(response))
					{
						tf.setText("Can't send, not your turn");
						return;
					}
					if(Model.getMove() != Constants.EMPTY_MOVE) {
						Model.sendMove();
						String messageFromServer = Model.listenToServer();
						
						System.out.println(messageFromServer);
						
						if(messageFromServer.equals(Constants.DEBUG_SERVER_RESPONSE_INVALID))
						{
							tf.setText("Something went wrong with the server! Please try again!");
							return;
						}
						
						if(Model.checkForTie(messageFromServer))
						{
							tf.setText("You got a tie!");
						}
						
						else if(Model.checkForWinIfNoTie(messageFromServer))
						{
							tf.setText("You Won!");
						}
						
						else
						{
							tf.setText("You Lost!");
						}
					}
					else tf.setText("Please choose an input first!");
				}
//				If connection is lost from the server, then this program will exit
				catch(SocketException e1) {
					System.err.println("Connection to server lost, exiting the program...");
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
					System.exit(1);
				}
//				This is for other errors, is purposely made more general because of the uncertain way the socket implementation deals with disconnects.
				catch (IOException | InvalidParameterException e2) {
					System.err.println("Exception occured");
					e2.printStackTrace();
				}
			}
		});
		//add each buttons into the BorderLayout
		frame.add(rock,BorderLayout.WEST);
		frame.add(paper,BorderLayout.CENTER);
		frame.add(scissor,BorderLayout.EAST);
		frame.add(outputToServer,BorderLayout.SOUTH);
		frame.add(tf,BorderLayout.NORTH);
		frame.setVisible(true); 

	}
}