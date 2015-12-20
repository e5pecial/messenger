import java.util.LinkedHashMap;
import java.util.Map;


public class ParseCookie
{
    private Map<String,String> map;

    public ParseCookie (String cookie)
    {
        try
        {
            this.map = new LinkedHashMap<String, String>();

            cookie = cookie.substring(8, cookie.length());

            String[] pairs = cookie.split("; ");

            for (String pair : pairs)
            {
                int idx = pair.indexOf("=");
                map.put((pair.substring(0, idx)),
                        (pair.substring(idx + 1)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public Map<String,String> getResult ()
    {
        return map;
    }
}
