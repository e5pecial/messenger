import javax.transaction.SystemException;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hibernate.Factory;
import hibernate.Message;

public class ClientThread implements Runnable
{
    private Socket _socket;
    private InputStream _inputStream;
    private OutputStream _outputStream;
    private String _contentType;
    private String _requestString;
    private String _path;
    private String _postBody;
    private String _action;
    private Integer _status;
    private String _cookie;
    private static List<String> hiddenFiles;
    private String _fileName;
    // класс для выполнения действий, которые просит клент
    private ActionType _actionType;

    public ClientThread(Socket socket) throws IOException
    {
        this._socket = socket;
        this._inputStream = _socket.getInputStream();
        this._outputStream = _socket.getOutputStream();
        this._contentType = "Content-Type: text/html";
        this._path = new File("").getAbsolutePath() + File.separator
                + "src" + File.separator + "res" +
                File.separator;
        this._postBody = "";
        this._status = 0;
        this._cookie = "";
        this._action = "";
        this._actionType = null;
        this.hiddenFiles = new ArrayList<>();
        hiddenFiles.add("users.html");
        hiddenFiles.add("chat.html");
        hiddenFiles.add("profile.html");
        hiddenFiles.add("template.html");
        hiddenFiles.add("templateChat.html");
    }

    public void run()
    {
        try
        {
            readHeaders();
            String fileName = getFileName(_requestString);
            if (fileName == null)
            {
                write400Response();
            }

            makeFileWithTemplate(fileName);
            readFromFile(fileName);
        }
        catch (Exception e)
        {

        } finally
        {
            try
            {
                _socket.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    private void writeResponse(byte[] bytes) throws IOException
    {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: add time\r\n" +
                _contentType + "\r\n" +
                "Content-Length: " + bytes.length + "\r\n" +
                "Connection: keep-alive\r\n\r\n";
        _outputStream.write(response.getBytes());
        _outputStream.write(bytes);
        _outputStream.flush();
    }

    private void readHeaders() throws IOException, InterruptedException
    {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(_inputStream));

        int count = 1;
        String headers = "";
        while (true)
        {
            String line = bufferedReader.readLine();

            if (count++ == 1)
            {
                _requestString = line;
            }
            if (line.startsWith("Cookie"))
            {
                _cookie = line;
            }
            headers += line;
            System.out.println(line);
            if (line.length() == 0)
            {
                break;
            }
            sleep(50);
        }

        // считывание тела post запроса
        if (_requestString.startsWith("POST"))
        {
            int indexBegin = headers.indexOf("Content-Length: ") + 16;
            int indexEnd = indexBegin;
            while (isDigit(headers.charAt(indexEnd)))
            {
                ++indexEnd;
            }
            try
            {
                Integer length = new Integer(headers.substring(indexBegin, indexEnd));
                Integer i = 0;
                for (i = 0; i < length; ++i)
                {
                    _postBody += (char)bufferedReader.read();
                }
                System.out.println("Post body");
                System.out.println(_postBody);
            }catch (Exception e)
            {
                System.out.print("Uncorrected string format for strToInt()");
            }
        }
    }

    private void readFromFile(String path) throws IOException, TemplateException
    {
        path = _path + path;
        File file = new File(path);

        if (file.exists())
        {
            try
            {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buff = new byte[(int)file.length()];
                fileInputStream.read(buff);
                writeResponse(buff);
                fileInputStream.close();
            }
            catch(Exception e)
            {
                System.out.println("readFromFile exception");
            }
        }
        else
        {
            write404Response();
        }
    }

    private void makeFileWithTemplate(String path) throws SQLException
    {
        path = _path + path;
        if (path.endsWith(".html"))
        {
            if (path.endsWith("users.html"))
            {
                try
                {
                    String header = readHeadersFromFile();
                    List userList = Factory.getFactory().getUserDAO().getAllUsers();

                    HashMap<String, Object> renderContent = new HashMap<>();
                    renderContent.put("header", header);
                    renderContent.put("list", userList);

                    Configuration cfg = new Configuration();
                    Template template = cfg.getTemplate("src/res/templates/" + _fileName);

                    // перезаписываем файл
                    Writer fileNew = new FileWriter(path);

                    // собираем шаблоны);
                    template.process(renderContent, fileNew);
                    fileNew.flush();
                    fileNew.close();
                }
                catch (Exception e)
                {
                    System.out.println("Error make file with template");
                }
            }
            else if (path.endsWith("chat.html"))
            {
                String header = readHeadersFromFile();
                ParseCookie cookie = new ParseCookie(_cookie);
                List<Message> messages;
//                List<Message> messagesRevers;
                try
                {
                    HashMap<String, Object> renderContent = new HashMap<>();
                    renderContent.put("header", header);

                    messages = Factory.getFactory().getMessageDAO().getMessages(
                            cookie.getResult().get("username"),
                            cookie.getResult().get("userto"));

//                    messagesRevers = Factory.getFactory().getMessageDAO().getMessages(
//                            cookie.getResult().get("userto"),
//                            cookie.getResult().get("username"));

                    renderContent.put("list", messages);
//                    renderContent.put("list_revers", messagesRevers);

                    Configuration cfg = new Configuration();
                    Template template = cfg.getTemplate("src/res/templates/" + _fileName);

                    // перезаписываем файл
                    Writer fileNew = new FileWriter(path);

                    // собираем шаблоны);
                    template.process(renderContent, fileNew);
                    fileNew.flush();
                    fileNew.close();
                }
                catch (Exception e)
                {
                    System.out.println("Error make file with template");
                }
            }
            else if (path.endsWith("profile.html"))
            {
                try
                {
                    String header = readHeadersFromFile();

                    HashMap<String, String> renderContent = new HashMap<>();
                    renderContent.put("header", header);

                    Configuration cfg = new Configuration();
                    Template template = cfg.getTemplate("src/res/templates/" + _fileName);

                    // перезаписываем файл
                    Writer fileNew = new FileWriter(path);

                    // собираем шаблоны);
                    template.process(renderContent, fileNew);

                    fileNew.flush();
                    fileNew.close();
                }
                catch (Exception e)
                {
                    System.out.println("Error make file with template");
                }
            }
        }

    }

    private String getFileName(String header) throws SystemException,
                                                        IOException,
                                                    SQLException
    {
        boolean isGet = false, isPost = false;
        int indexBegin = 0, indexEnd = 0;
        String fileName = "";
        if (header.startsWith("GET"))
        {
            if (header.equals("GET / HTTP/1.1"))
            {
                _contentType = "Content-Type: text/html";
                return "auth.html";
            }

            isGet = true;
            indexBegin = 5;
        }
        else if (header.startsWith("POST"))
        {
            isPost = true;
            indexBegin = 6;
        }
        else
        {
            return null; // кидаем 400
        }

        if (header.indexOf("?") > 0) // значит, что присутствует query string
        {
            indexEnd = header.indexOf("?");
            ParseQuery parseQuery = new ParseQuery(header.substring(indexEnd + 1, header.length() - 9));
            _action = parseQuery.getResult().get("action");

            _actionType = new ActionType(_action, _postBody, _cookie);
            _status = _actionType.getCode();

            if (_status == 400)
            {
                write400Response();
            }
            else if (_status == 401)
            {
                write401Response();
            }
        }
        else
        {
            indexEnd = header.length() - 9;
        }

        fileName = header.substring(indexBegin, indexEnd);
        _fileName = header.substring(indexBegin, indexEnd);

        // смотрим может ли пользователь ходить по файлам
        Supervisor supervisor = new Supervisor(_cookie, fileName);
        if (supervisor.isHidden() && !supervisor.mayComIn())
        {
            write401Response();
            return "";
        }
        else
        {
            if (fileName.endsWith(".html"))
            {
                _contentType = "Content-Type: text/html";
            }
            else if (fileName.endsWith(".css"))
            {
                _contentType = "Content-Type: text/css";
            }
            else if (fileName.endsWith(".js"))
            {
                _contentType = "Content-Type: text/js";
            }

            return fileName;
        }
    }

    private void write400Response() throws  IOException
    {
        String response = "HTTP/1.1 400 Bad Request\r\n" +
                "Date: add time\r\n" +
                "Connection: close\r\n";
        _outputStream.write(response.getBytes());
        _outputStream.flush();
        _socket.close();
    }

    private  void write404Response() throws IOException
    {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Date: add time\r\n" +
                "Connection: close\r\n\r\n";
        _outputStream.write(response.getBytes());
        _outputStream.write(("<html><head><title>404</title></head><body>" +
                            "<h1>404 Not Found</h1></body></html>").getBytes());
        _outputStream.flush();
        _socket.close();
    }

    private  void write401Response() throws IOException
    {
        String response = "HTTP/1.1 401 Unauthorized\r\n" +
                "Date: add time\r\n" +
                "Connection: close\r\n\r\n";
        _outputStream.write(response.getBytes());
        _outputStream.flush();
        _socket.close();
    }

    boolean isDigit(char ch)
    {
        return ((int)ch > 47 && (int)ch < 58);
    }

    private String readHeadersFromFile()
    {
        String path = "src/res/templates/header.html";

        StringBuilder sb = new StringBuilder();

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(path));
            try
            {
                String s;
                while ((s = in.readLine()) != null)
                {
                    sb.append(s);
                    sb.append("\n");
                }
            }
            finally
            {
                in.close();
            }
        }
        catch(IOException e)
        {

        }
        return sb.toString();
    }
}
