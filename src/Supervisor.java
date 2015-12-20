import hibernate.Factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Supervisor
{
    private String _cookie;
    private String _path;
    private boolean fileIsHidden;
    private static List<String> files;

    public Supervisor(String cookie, String path)
    {
        this._cookie = cookie;
        this._path = path;
        this.fileIsHidden = false;
        this.files = new ArrayList<>();
        files.add("users.html");
        files.add("chat.html");
        files.add("profile.html");
        files.add("template.html");
        files.add("templateChat.html");

        for (int i = 0; i < files.size(); ++i)
        {
            if (_path.equals(files.get(i)))
            {
                fileIsHidden = true;
                break;
            }
        }
    }

    public boolean mayComIn() throws SQLException
    {
        if (fileIsHidden)
        {
            ParseCookie cookie = new ParseCookie(_cookie);
            if (Factory.getFactory().getUserDAO().isExist(
                    cookie.getResult().get("username"),
                    cookie.getResult().get("password")
            ) != null)
            {
                System.out.println("may come in");
                return true;
            }
            else
            {
                System.out.println("go away");
                return false;
            }
        }
        else
        {
            System.out.println("may come in");
            return true;
        }
    }

    public boolean isHidden()
    {
        return fileIsHidden;
    }
}
