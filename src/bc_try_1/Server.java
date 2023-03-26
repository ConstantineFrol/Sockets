package bc_try_1;

import java.net.ServerSocket;


public class Server {

    private static int threadCount = 0;
    public static void main(String[] args) {
        int serverPort = 4443;    // default port
        String message;

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a stream socket for accepting
            //   connections
            ServerSocket myConnectionSocket =
                    new ServerSocket(serverPort);
            /**/
            System.out.println("The server is ready.");
            while (true) {  // forever loop
                // wait to accept a connection
                /**/
                System.out.println("Waiting for a connection.");
                SSSocket myDataSocket = new SSSocket
                        (myConnectionSocket.accept());
                /**/
                System.out.println("connection accepted");
                // Start a thread to handle this client's session
                Thread theThread =
                        new Thread(new Ser_Thread(myDataSocket));
                theThread.start();
                threadCount++;

                try {
                    theThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!theThread.isAlive()) {
                    System.out.println("Client: " + threadCount + " has completed its execution.");
                }
                // and go on to the next client
            } //end while forever
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    } //end main
} // end class
