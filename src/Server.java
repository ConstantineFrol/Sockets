import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(3000);
        Socket socket = serverSocket.accept();
        Scanner in = new Scanner(socket.getInputStream());
        while(in.hasNext()){
            System.out.println(in.nextLine());
        }
        in.close();
        socket.close();
        serverSocket.close();
    }
}
