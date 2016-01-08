package ro.pub.cs.aipi.lab10.general;

public interface Constants {

	public static final boolean DEBUG                            = true;

	public static final String  INITIAL_CONTEXT_FACTORY_VALUE    = "com.sun.enterprise.naming.SerialInitContextFactory";
	public static final String  URL_PKG_PREFIXES_VALUE           = "com.sun.enterprise.naming";
	public static final String  STATE_FACTORIES_VALUE            = "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl";
	public static final String  PROVIDER_URL_VALUE               = "iiop://localhost:3700";

	public static final String  TOPIC_CONNECTION_FACTORY_NAME    = "MessagingTopicConnectionFactory";
	public static final String  TOPIC_NAME                       = "jms/MessagingTopic";

	public static final long    MESSAGE_ERROR_TIMEOUT            = 5000;
	
	public static final String  RESOURCES_PATH                   = "/resources";
	public static final String  FXML_PATH                        = RESOURCES_PATH + "/fxmls";
	public static final String  IMAGE_PATH                       = RESOURCES_PATH + "/images";

	public static final String  APPLICATION_TITLE                = "Messaging Client";
	public static final String  ICON_FILE_NAME                   = IMAGE_PATH + "/icon.png";

	public static final String  ABOUT_FXML_FILE                  = FXML_PATH + "/about.fxml";
	public static final String  ABOUT_TEXT                       = "Messaging Client\n(c) Aplicatii Integrate pentru Intreprinderi 2015\nv1.1";

	public static final String  AUTHENTICATION_FXML_FILE         = FXML_PATH + "/authentication.fxml";
	public static final String  NO_USERNAME_ERROR                = "You must fill in the username!";

	public static final String  CONTACTS_LIST_FXML_FILE          = FXML_PATH + "/contactslist.fxml";
	public static final int     OPERATIONS_MENU_INDEX            = 0;
	public static final int     HELP_MENU_INDEX                  = 1;
	public static final int     COMMUNICATE_MENU_INDEX           = 0;
	public static final int     CLOSE_MENU_INDEX                 = 1;
	public static final int     ABOUT_MENU_INDEX                 = 0;
	public static final String  CONTACTS_LIST                    = "Contacts List";
	public static final String  CONTACTS_LIST_ICON               = IMAGE_PATH + "/contactslist.png";
	public static final String  CONNECTED_CONTACTS_LIST          = "Connected";
	public static final String  CONNECTED_CONTACTS_LIST_ICON     = IMAGE_PATH + "/connectedcontactslist.png";
	public static final String  RECENT_CONTACTS_LIST             = "Recent";
	public static final String  RECENT_CONTACTS_LIST_ICON        = IMAGE_PATH + "/recentcontactslist.png";

	public static final String  MESSAGE_EXCHANGE_FXML_FILE       = FXML_PATH + "/messageexchange.fxml";
	public static final String  EMPTY_STRING                     = "";
	public static final String  NO_INTERLOCUTOR_MESSAGE_ERROR    = "You must fill in\nrecipient username and message!";

	public static final String  CUSTOM_MESSAGE_PROPERTY          = "recipientusername";

	public static final String  LOGIN_MESSAGE                    = "active";
	public static final String  LOGOUT_MESSAGE                   = "inactive";

	public static final String  CARRIAGE_RETURN                  = "\r";
	public static final String  LINE_FEED                        = "\n";
	public static final String  NEW_LINE                         = CARRIAGE_RETURN + LINE_FEED;
	
	public static final String  TEMPORARY_DIRECTORY              = "./temp/";

}
