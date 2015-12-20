import hibernate.Factory;
import hibernate.Message;
import hibernate.User;

import javax.transaction.SystemException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class ActionType
{
    private String _actionType;
    private String _postBody;
    private String _userName;
    private String _cookie;
    private int code;
    private List<Message> messages;

    public ActionType()
    {
        _actionType = null;
    }

    public ActionType(String actionType, String postBody) throws UnsupportedEncodingException, SystemException
    {
        _actionType = actionType;
        _postBody = postBody;
        _userName = null;
        _cookie = null;
        code = 0;
        messages = null;
        analysis();
    }

    public ActionType(String actionType, String postBody,
                      String cookie) throws UnsupportedEncodingException, SystemException
    {
        _actionType = actionType;
        _postBody = postBody;
        _cookie = cookie;
        _userName = null;
        code = 0;
        analysis();
    }

    private void analysis() throws UnsupportedEncodingException, SystemException
    {
        // создание записи о юзере в БД
        if (_actionType.equals("reg"))
        {
            ParseQuery parseQuery = new ParseQuery(URLDecoder.decode(_postBody, "utf-8"));
            User user = null;
            try
            {
                user = new User(parseQuery.getResult().get("username"),
                        parseQuery.getResult().get("password"));
                _userName = Factory.getFactory().getUserDAO().addUser(user); // добавляем в БД
                code = 200;
            }
            catch (Exception e)
            {
                System.out.println("Error add Users object");
                code = 400;
            }
        }

        // авторизация и поиск по БД
        else if (_actionType.equals("auth"))
        {
            ParseQuery parseQuery = new ParseQuery(URLDecoder.decode(_postBody, "utf-8"));
            try
            {
                if (Factory.getFactory().getUserDAO().isExist(
                        parseQuery.getResult().get("username"),
                        parseQuery.getResult().get("password")
                        ) != null)
                {
                    System.out.println("Auth success");
                    code = 200;
                }
                else
                {
                    System.out.println("Auth fail");
                    code = 401;
                }
            }
            catch (Exception e)
            {
                System.out.println("Error add Users object");
                code = 401;
            }
        }

        // запись сообщения в базу
        else if (_actionType.equals("letter"))
        {
            ParseQuery parseQuery = new ParseQuery(URLDecoder.decode(_postBody, "utf-8"));
            try
            {
                Factory.getFactory().getMessageDAO().addMessage(parseQuery.getResult().get("userfrom"),
                                                                parseQuery.getResult().get("userto"),
                                                                parseQuery.getResult().get("letter")
                                                                );
                code = 200;
            }
            catch (Exception e)
            {
                System.out.println("Error add Users object");
                code = 401;
            }
        }

        // подгрузка истории чата
        else if (_actionType.equals("chat_history"))
        {
            ParseQuery parseQuery = new ParseQuery(URLDecoder.decode(_postBody, "utf-8"));
            ParseCookie cookie = new ParseCookie(_cookie) ;
            try
            {
                messages = Factory.getFactory().getMessageDAO().getMessages(
                                                                cookie.getResult().get("username"),
                                                                cookie.getResult().get("userto"));


                for (int i = 0; i < messages.size(); ++i)
                {
                    System.out.println(messages.get(i).getHistoryMessage());
                }
                code = 200;
            }
            catch (Exception e)
            {
                System.out.println("Error get chat history");
                code = 401;
            }
        }

    }

    public String getUserName()
    {
        return this._userName;
    }

    public  int getCode()
    {
        return code;
    }

    public List<Message> getMessages()
    {
        return messages;
    }
}
