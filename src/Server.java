import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;


public class Server
{
    private ServerSocket _serverSocket;
    private int _port;

    public Server(int port) throws IOException
    {
        this._port = port;
        _serverSocket = new ServerSocket(_port, 10);
    }

    public void connection() throws IOException, InterruptedException
    {
        while (true)
        {
            Socket socket = _serverSocket.accept();
            System.out.println("Client accepted");
            new Thread(new ClientThread(socket)).start();
            sleep(50);
        }
    }
}
