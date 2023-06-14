package chatServer;
import java.io.*;
import java.net.Socket;
import java.util.List;
import javax.net.ssl.*;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String username;
    private List<ClientHandler> clients;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            username = (String) objectInputStream.readObject();
            String userJoinedMessageString = "User " + username + " Memasuki Chat."; 
            System.out.println(userJoinedMessageString);

            messages.Messages message;
            do {
                message = (messages.Messages) objectInputStream.readObject();

                if (message.getReceiver() == null) {
                    ClientInputHandler.broadcast(message, clients);
                } else if (message.getReceiver().equals("server") && message.getMessageContent().equals("getOnlineUsers")) {
                    List<String> onlineUsers = ClientInputHandler.getOnlineUsers(clients);
                    int userCount = 1;
                    String serverMessage = "";
                    for (String user : onlineUsers) {
                        System.out.println(user);
                        serverMessage = serverMessage.concat(Integer.toString(userCount) + "." + user + "\n");
                        userCount++;
                    }
                    System.out.println(serverMessage);
                    messages.Messages onlineUsersMessage = new messages.Messages("server", username, serverMessage);
                    sendMessage(onlineUsersMessage);
                } else {
                    // Private message
                    ClientInputHandler.sendPrivateMessage(username, message.getReceiver(), message, clients);
                }

            } while (!message.getMessageContent().equals("bye"));
            messages.Messages terminationMessage = new messages.Messages("server", username, "bye");
            sendMessage(terminationMessage);
            clients.remove(this);

            String terminationNotificationString = "User " + username + " meninggalkan chat.";
            System.out.println(terminationNotificationString);

            messages.Messages terminationNotificationMessage = new messages.Messages("server", terminationNotificationString);
            ClientInputHandler.broadcast(terminationNotificationMessage, clients);

            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(messages.Messages message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}