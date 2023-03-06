import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Expect 2 arguments got 1");
        } else if (args.length > 2) {
            System.out.println("Got too many arguments");
        }else {
            System.out.println("Processing...");
            try{
                // Incoming Data from Client
                int connectionPort = Integer.parseInt(args[0]);
                String message_from_client = args[1];

                // Create connection
                ServerSocket connectionSocket = new ServerSocket(connectionPort);
                System.out.println("Waiting for Client to Connect on port no.: " + connectionPort);

                Socket data_from_socket = connectionSocket.accept();
                System.out.println("Connection established on port no.: " + data_from_socket.getLocalPort());

            }catch (Exception e){
                System.out.println(e.getMessage().toUpperCase());
            }
        }
    }
}
