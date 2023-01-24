import java.io.IOException;
import java.net.*;

public class MyDatagramSocket extends DatagramSocket {

    // Parameters /////////////////
    static final int MAX_LEN = 100;
    ///////////////////////////////


    // Constructors /////////////////////////////////////////////
    public MyDatagramSocket(int portNo) throws SocketException {
        super(portNo);
    }

    public MyDatagramSocket() throws SocketException {
    }
    //////////////////////////////////////////////////////////////

    // Functions ///////////////////////////////////////////////////////////////////////////////////////////////
    public void sendMessage(InetAddress receiverHost, int receiverPort, String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, receiverHost, receiverPort);
        this.send(datagram);
    }

    public String receiveMessage() throws IOException {
        byte[] receiveBuffer = new byte[MAX_LEN];
        DatagramPacket datagram = new DatagramPacket(receiveBuffer, MAX_LEN);
        this.receive(datagram);
        String message = new String(receiveBuffer);
        return message;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
}