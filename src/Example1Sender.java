import java.net.*;
import java.io.*;

/**
 * This example illustrates the basic method calls for connectionless
 * datagram socket.
 *
 * @author M. L. Liu
 */
public class Example1Sender extends MyDatagramSocket{
    public Example1Sender(int portNo) throws SocketException {
        super(portNo);
    }

    // An application which sends a message using connectionless
// datagram socket.
// Three command line arguments are expected, in order: 
//    <domain name or IP address of the receiver>
//    <port number of the receiver's socket>
//    <message, a string, to send>
    public static void main(String[] args) {
        if (args.length != 3)
            System.out.println
                    ("This program requires three command line arguments");
        else {
            try {
                MyDatagramSocket sender = new MyDatagramSocket();
                sender.sendMessage(InetAddress.getLocalHost(), 3000, "LOL");
            } // end try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } // end else
    } // end main
} // end class