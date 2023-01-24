import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3000);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("Hello");
        out.println("World!");
        out.println("How are you?");

        out.close();
        socket.close();
    }
}
