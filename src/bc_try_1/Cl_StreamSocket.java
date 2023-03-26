package bc_try_1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


public class Cl_StreamSocket extends Socket {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    Cl_StreamSocket(InetAddress acceptorHost,
                    int acceptorPort) throws SocketException,
            IOException {
        socket = new Socket(acceptorHost, acceptorPort);
        setStreams();

    }

    Cl_StreamSocket(Socket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        // get an input stream for reading from the data socket
        InputStream inStream = socket.getInputStream();
        input =
                new BufferedReader(new InputStreamReader(inStream));
        OutputStream outStream = socket.getOutputStream();
        // create a PrinterWriter object for character-mode output
        output =
                new PrintWriter(new OutputStreamWriter(outStream));
    }

    public void sendMessage(String message)
            throws IOException {
        output.print(message + "\n");
        //The ensuing flush method call is necessary for the data to
        // be written to the socket data stream before the
        // socket is closed.
        output.flush();
    } // end sendMessage

    public String receiveMessage()
            throws IOException {
        // read a line from the data stream
        String message = input.readLine();
        return message;
    } //end receiveMessage

    public void close()
            throws IOException {
        socket.close();
    }
} //end class