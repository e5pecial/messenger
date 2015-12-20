package hibernate.manage;

import hibernate.HibernateUtil;
import hibernate.Message;
import hibernate.MessageDAO;
import hibernate.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.*;


public class ManageMessages implements MessageDAO
{
    public void addMessage(String userFrom, String userTo, String message)
    {
        Session session = null;
        Message msg = new Message(userTo, message);
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            // находим отпарвителя в базе
            User user = null;
            List users = session.createQuery("FROM User").list();
            for (Iterator itr = users.iterator(); itr.hasNext();)
            {
                User u = (User)itr.next();
                if (u.getUserName().equals(userFrom))
                {
                    user = u;
                    break;
                }
            }

            msg.setUser(user);
            Calendar calendar = new GregorianCalendar().getInstance();
            msg.setCalendar(calendar.getTime().toString());
            user.getMessages().add(msg);

            session.save(msg);
            session.getTransaction().commit();
        }
        catch (HibernateException e)
        {
            System.out.println("Error add message in DB");
        }
        finally
        {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    public List<Message> getMessages(String userFrom, String userTo)
    {
        Session session = null;
        List<Message> messages = null;
        try
        {
            messages = new ArrayList<>();
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            // находим отправителя в базе
            List allMessages = session.createQuery("FROM Message").list();
            for (Iterator itr = allMessages.iterator(); itr.hasNext();)
            {
                Message message = (Message)itr.next();

            if (message.getUser().getUserName().equals(userFrom) &&
                message.getUserTo().equals(userTo) ||
                message.getUser().getUserName().equals(userTo) &&
                message.getUserTo().equals(userFrom))
                {
                    Message msg = new Message(message.getUserTo(), message.getMessage());
                    msg.setUser(message.getUser());
                    msg.setCalendar(message.getCalendar());
                    messages.add(msg);
                }
            }

            session.getTransaction().commit();
        }
        catch (HibernateException e)
        {
            System.out.println("Error add message in DB");
            return null;
        }
        finally
        {
            if(session != null && session.isOpen())
            {
                session.close();
            }
            return messages;
        }
    }
}
