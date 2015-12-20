import java.io.IOException;

public class MainClass
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Server myServer = new Server(3000);
        myServer.connection();
    }
}
