package ro.pub.cs.aipi.lab10.communicator;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Properties;

import javafx.application.Platform;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ro.pub.cs.aipi.lab10.general.Constants;
import ro.pub.cs.aipi.lab10.graphicuserinterface.ContactsList;

public class PublishSubscribe implements MessageListener {

    private Context initialContext = null;
    private TopicConnectionFactory topicConnectionFactory;
    private TopicConnection topicConnection;
    private Topic topic;

    private TopicSession publishSession;
    private TopicPublisher topicPublisher;

    private String userName;
    private ContactsList contactsList;

    public PublishSubscribe(String userName, ContactsList contactsList) {
        try {
            this.userName = userName;
            this.contactsList = contactsList;
            initialContext = getInitialContext();
            topicConnectionFactory = (TopicConnectionFactory) initialContext.lookup(Constants.TOPIC_CONNECTION_FACTORY_NAME);
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicConnection.start();
            topic = (Topic) initialContext.lookup(Constants.TOPIC_NAME);
            subscribe();
            publishSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicPublisher = publishSession.createPublisher(topic);
        } catch (NamingException | JMSException exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }

    public Context getInitialContext() throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, Constants.INITIAL_CONTEXT_FACTORY_VALUE);
        properties.setProperty(Context.URL_PKG_PREFIXES, Constants.URL_PKG_PREFIXES_VALUE);
        properties.setProperty(Context.STATE_FACTORIES, Constants.STATE_FACTORIES_VALUE);
        properties.setProperty(Context.PROVIDER_URL, Constants.PROVIDER_URL_VALUE);
        return new InitialContext(properties);
    }

    public void handleMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            final CustomMessage customMessage = (CustomMessage) objectMessage.getObject();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // TODO (exercise 9): include the date and time of the message
                    contactsList.handleMessage(
                            customMessage.getSenderUserName(),
                            customMessage.getMessageContent(),
                            customMessage.getMessageDateTime());
                }
            });
        } catch (JMSException exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        handleMessage(message);
    }

    public void publish(String receiverUserName, String messageContent) throws JMSException, IOException {
        ObjectMessage objectMessage = publishSession.createObjectMessage();

        // TODO (exercise 9): include the date and time of the message
        // use the current date and time (default GregorianCalendar constructor)
        objectMessage.setObject(new CustomMessage(userName, messageContent, new GregorianCalendar()));
        objectMessage.setStringProperty(Constants.CUSTOM_MESSAGE_PROPERTY,
                receiverUserName);
        topicPublisher.publish(objectMessage);
    }

    public void subscribe() throws JMSException {
        TopicSession subscribeSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        // TODO (exercise 8): change the consumer behavior
        // in order for the user to receive the messages while being offline
        MessageConsumer consumer = subscribeSession.createSharedDurableConsumer(topic, userName, Constants.CUSTOM_MESSAGE_PROPERTY + "= '" + userName + "'");

        consumer.setMessageListener(this);
    }

    public void close() {
        try {
            topicConnection.close();
        } catch (JMSException exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }
}
