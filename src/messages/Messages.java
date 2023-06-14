package messages;

import java.io.Serializable;

public class Messages implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sender;
    private String receiver;
    private String messageContent;
    private boolean Private;

    public Messages(String sender, String messageContent) {
        this.sender = sender;
        this.messageContent = messageContent;
        Private = false;
    }

    public Messages(String sender, String receiver, String messageContent) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
        Private = true;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public boolean isPrivate() {
        return Private;
    }
}