import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;


public class ParseQuery
{
    private Map<String,String> map;

    public ParseQuery (String query) 
    {
        try
        {
            this.map = new LinkedHashMap<String, String>();

            String[] pairs = query.split("&");

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
