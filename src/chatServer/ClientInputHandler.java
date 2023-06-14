package chatServer;

import java.util.ArrayList;
import java.util.List;

public class ClientInputHandler {
    public static void broadcast(messages.Messages message, List<ClientHandler> clients) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static List<String> getOnlineUsers(List<ClientHandler> clients) {
        List<String> onlineUsers = new ArrayList<>();
        for (ClientHandler client : clients) {
            onlineUsers.add(client.getUsername());
        }
        return onlineUsers;
    }

    public static void sendPrivateMessage(String sender, String receiver, messages.Messages message,
            List<ClientHandler> clients) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(receiver)) {
                client.sendMessage(message);
                break;
            }
        }
    }
}
