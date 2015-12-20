package hibernate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "users")
public class User
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "user_id")
    private Integer _id;

    @Column (name = "user_name")
    private String _userName;

    @Column (name = "password")
    private String _password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Message> messages;

    private static Integer countUsers = 0;

    public User()
    {
        _id = null;
        _userName = null;
        _password = null;
    }

    public User(String userName, String password)
    {
        _userName = userName;
        _password = password;
        _id = ++countUsers;
    }

    public Integer getId()
    {
        return _id;
    }

    public void setId(Integer id)
    {
        _id = id;
    }

    public String getUserName()
    {
        return  _userName;
    }

    public void setUserName(String userName)
    {
        _userName = userName;
    }

    public String getPassword()
    {
        return  _password;
    }

    public void setPassword(String password)
    {
        _password = password;
    }

    public boolean equals(User user)
    {
        return this.getUserName().equals(user.getUserName()) &&
                this.getPassword().equals(user.getPassword());
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }
}