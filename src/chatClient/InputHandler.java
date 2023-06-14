package chatClient;

import java.io.*;
import java.util.Scanner;

public class InputHandler extends Thread {
    private BufferedReader reader;
    private ObjectOutputStream oos;
    private String username;
    private Scanner sc;
    public InputHandler(BufferedReader reader, ObjectOutputStream oos, String username) {
        this.reader = reader;
        this.oos = oos;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String input = reader.readLine();
//                input = sc.nextLine();

                String[] inputParts = input.split(" ", 3);
                String command = inputParts[0].toLowerCase();

                if (command.equals("users")) {
                    messages.Messages requestUsersMessage = new messages.Messages(username, "server", "getOnlineUsers");
                    oos.writeObject(requestUsersMessage);
                    oos.flush();
                } else if (command.equals("private")) {
                    if (inputParts.length != 3) {
                        System.out.println("Invalid format. Usage: private <receiver> <message>");
                    } else {
                        String receiver = inputParts[1];
                        String content = inputParts[2];
                        messages.Messages privateMessage = new messages.Messages(username, receiver, content);
                        oos.writeObject(privateMessage);
                        oos.flush();
                    }
                } else if (command.equals("quit")) {
                    // remove user from online users
                	messages.Messages exitMessage = new messages.Messages(username, "server", "bye");
                    oos.writeObject(exitMessage);
                    oos.flush();
                    break;
                } else {
                	messages.Messages broadcastMessage = new messages.Messages(username, null, input);
                    oos.writeObject(broadcastMessage);
                    oos.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}