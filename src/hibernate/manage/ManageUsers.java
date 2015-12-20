package hibernate.manage;

import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


public class ManageUsers implements UserDAO
{
    public String addUser(User user) throws SQLException
    {
        String userName = null;
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            userName = user.getUserName();
            session.save(user);
            session.getTransaction().commit();
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
        return userName;
    }

    public void deleteUser(Integer userId)
    {
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            User user = (User)session.get(User.class, userId);
            session.delete(user);
            session.getTransaction().commit();
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    public String updateUser(Integer userId, String newUserName,
                           String newPassword)
    {
        Session session = null;
        String userName = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            User user = (User)session.get(User.class, userId);
            user.setUserName(newUserName);
            user.setPassword(newPassword);
            userName = user.getUserName();

            session.update(user);
            session.getTransaction().commit();
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
            return userName;
        }
    }

    public String isExist(String userName, String password)
    {
        Session session = null;
        String flag = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            User user = new User(userName, password);
            List users = session.createQuery("FROM User").list();
            for (Iterator itr = users.iterator(); itr.hasNext();)
            {
                User u = (User)itr.next();
                if (user.equals(u))
                {
                    flag = userName;
                    break;
                }
            }
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
            return flag;
        }
    }

    public List getAllUsers()
    {
        Session session = null;
        List users = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            users = session.createQuery("FROM User").list();
//            for (Iterator itr = users.iterator(); itr.hasNext();)
//            {
//                User user = (User)itr.next();
//                System.out.println("\nUserName: " + user.getUserName());
//                System.out.print(" Password: " + user.getPassword());
//            }

            session.getTransaction().commit();
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return users;
    }
}
