package hibernate;

import hibernate.manage.ManageMessages;
import hibernate.manage.ManageUsers;


public class Factory
{
    private static UserDAO userDAO = null;
    private static MessageDAO messageDAO = null;
    private static Factory factory = null;

    public static synchronized Factory getFactory(){
        if(factory == null){
            factory = new Factory();
        }
        return factory;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new ManageUsers();
        }
        return userDAO;
    }

    public MessageDAO getMessageDAO() {
        if (messageDAO == null) {
            messageDAO = new ManageMessages();
        }
        return messageDAO;
    }
}
