package hibernate;

import java.sql.SQLException;
import java.util.List;


public interface MessageDAO {
    public void addMessage(String userFrom, String userTo, String message) throws SQLException;
    public List<Message> getMessages(String userFrom, String userTo);
}
