package bc_try_1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Cl_Helper {

    static final String endMessage = ".";
    private Cl_StreamSocket mySocket;
    private InetAddress serverHost;
    private int serverPort;

    Cl_Helper(String hostName,
              String portNum) throws SocketException,
            UnknownHostException, IOException {

        this.serverHost = InetAddress.getByName(hostName);
        this.serverPort = Integer.parseInt(portNum);
        //Instantiates a stream-mode socket and wait for a connection.
        this.mySocket = new Cl_StreamSocket(this.serverHost,
                this.serverPort);
        /**/
        System.out.println("Connection request made");
    } // end constructor

    public String getEcho(String message) throws SocketException,
            IOException {
        String echo = "";
        mySocket.sendMessage(message);
        // now receive the echo
        echo = mySocket.receiveMessage();
        return echo;
    } // end getEcho

    public void done() throws SocketException,
            IOException {
        mySocket.sendMessage(endMessage);
        mySocket.close();
    } // end done
} //end class

