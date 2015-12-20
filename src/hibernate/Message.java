package hibernate;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "messages")
public class Message
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "message_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_to")
    private String userTo;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private String time;

    private static Integer countId = 0;

    public Message()
    {
        userTo = null;
        message = null;
        id = null;
    }

    public Message(String userTo,
                    String message)
    {
        this.userTo = userTo;
        this.message = message;
        id = ++countId;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserTo()
    {
        return userTo;
    }

    public void setUserTo(String userTo)
    {
        this.userTo = userTo;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getCalendar()
    {
        return time;
    }

    public void setCalendar(String time)
    {
        this.time = time;
    }

    public String getHistoryMessage()
    {
        String result = user.getUserName() + "\n" + message + " " + time;
        return result;
    }
}
