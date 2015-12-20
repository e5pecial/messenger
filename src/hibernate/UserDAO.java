package hibernate;

import java.sql.SQLException;
import java.util.List;


public interface UserDAO
{
    public String addUser(User user) throws SQLException;
    public void deleteUser(Integer userId) throws SQLException;
    public String updateUser(Integer id, String newUserName, String newPassword);
    public String isExist(String email, String password) throws SQLException;
    public List getAllUsers() throws SQLException;
//    public List getAllId();
}
