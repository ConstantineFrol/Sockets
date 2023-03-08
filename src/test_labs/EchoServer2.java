//package test_labs;
//
//import java.net.ServerSocket;
//
///**
// * This module contains the application logic of an echo server
// * which uses a stream socket for interprocess  communication.
// * A command-line argument is required to specify the server port.
// *
// * @author M. L. Liu
// */
//public class EchoServer2 {
//    static final String endMessage = ".";
//    static final String LAST_MESSAGE = "Session is over.";
//
//    public static void main(String[] args) {
//        int serverPort = 3000;    // default port
//        String message;
//
//        if (args.length == 1)
//            serverPort = Integer.parseInt(args[0]);
//        try {
//            // instantiates a stream socket for accepting
//            //   connections
//            ServerSocket myConnectionSocket = new ServerSocket(serverPort);
//            /**/
//            System.out.println("Server is ready.");
//            while (true) {  // forever loop
//                // wait to accept a connection
//                /**/
//                System.out.println("Waiting for a connection.");
//                MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
//                /**/
//                System.out.println("connection accepted");
//                boolean done = false;
//                while (!done) {
//                    message = myDataSocket.receiveMessage();
//                    /**/
//                    System.out.println("message received: " + message);
//                    if ((message.trim()).equals(endMessage)) {
//                        //Session over; close the data socket.
//                        /**/
//                        System.out.println("Session over.");
//
//                        myDataSocket.sendMessage(LAST_MESSAGE);
//                        myDataSocket.close();
//                        done = true;
//                    } //end if
//                    else {
//                        // Now send the echo to the requestor
//                        myDataSocket.sendMessage(message);
//                    } //end else
//                } //end while !done
//            } //end while forever
//        } // end try
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    } //end main
//} // end class
package test_labs;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EchoServer2 {

    static final String endMessage = ".";
    static final String LAST_MESSAGE = "Session is over.";

    public static void main(String[] args) {
        int serverPort = 3000;    // default port
        String message;
        ArrayList<String> userData = new ArrayList<>(); // to store user inputs

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a stream socket for accepting connections
            ServerSocket myConnectionSocket = new ServerSocket(serverPort);

            System.out.println("Server is ready.");
            while (true) {  // forever loop
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());

                System.out.println("connection accepted");
                boolean done = false;
                while (!done) {
                    message = myDataSocket.receiveMessage();
                    System.out.println("message received: " + message);
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    userData.add(currentTime + " : " + message); // store the user input along with the time it was received

                    if ((message.trim()).equals(endMessage)) {
                        System.out.println("Session over.");
                        myDataSocket.sendMessage(LAST_MESSAGE);
                        myDataSocket.close();
                        done = true;
                    } else if (message.toUpperCase().equals("HISTORY")) {
                        String data = "history: \n";
                        for (String input : userData) {
                            data += input + " \n ";
                            myDataSocket.sendMessage(data);
                        }
                    } else if (message.toUpperCase().equals("HELP")) {
                        myDataSocket.sendMessage("Available commands:\nHELP, STR FILTER, NUM FILTER, HISTORY, END/QUIT.");
                    } else if (message.toUpperCase().equals("STR FILTER")) {
                        myDataSocket.sendMessage("Enter a string containing digits: ");
                        String str = myDataSocket.receiveMessage();
                        String result = removeDigits(str);
                        myDataSocket.sendMessage("String with digits removed: " + result);
                    } else if (message.toUpperCase().equals("NUM FILTER")) {
                        myDataSocket.sendMessage("Enter a string with digits: ");
                        String str = myDataSocket.receiveMessage();
                        String result = getDigits(str);
                        myDataSocket.sendMessage("Digits from the string: " + result);
                    } else if (message.toUpperCase().contains("end") || message.contains("quit")) {
                        myDataSocket.sendMessage("Session over.");
                        myDataSocket.close();
                        done = true;
                    } else {
                        myDataSocket.sendMessage("error: No such command. Available commands:\nSTR FILTER, NUM FILTER, HISTORY, END/QUIT.");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String removeDigits(String str) {
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String getDigits(String str) {
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }
}