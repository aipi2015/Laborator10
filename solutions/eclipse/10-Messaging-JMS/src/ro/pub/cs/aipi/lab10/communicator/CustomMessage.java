package ro.pub.cs.aipi.lab10.communicator;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class CustomMessage implements Serializable {

	private static final long serialVersionUID = 2015L;

	private String senderUserName;
	private String messageContent;
	
	// TODO (exercise 9): include the date and time of the message
	private GregorianCalendar messageDateTime;

	public CustomMessage(String senderUserName, String messageContent, GregorianCalendar messageDateTime) {
		this.senderUserName = senderUserName;
		this.messageContent = messageContent;
		this.messageDateTime = messageDateTime;
	}

	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	// TODO (exercise 9): include the date and time of the message
	// getters & setters
	public GregorianCalendar getMessageDateTime() {
		return messageDateTime;
	}
	
	public void setMessageDateTime(GregorianCalendar messageDateTime) {
		this.messageDateTime = messageDateTime;
	}

}
