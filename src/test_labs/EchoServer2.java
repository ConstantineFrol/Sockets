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
        String currentTime = "";
        ArrayList<String[]> messageHistory = new ArrayList<String[]>();
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

                    if (message != "") {
                        currentTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss,").format(new Date());
                    }

                    System.out.println("message received: " + message);
                    userData.add(currentTime + " : " + message); // store the user input along with the time it was received

                    if ((message.trim()).equals(endMessage)) {
                        System.out.println("Session over.");
                        myDataSocket.sendMessage(LAST_MESSAGE);
                        myDataSocket.close();
                        done = true;
                    } else if (message.trim().toUpperCase().equals("HISTORY")) {
                        String data = "history: ";
                        for (String input : userData) {
                            data += input + ", ";
                            myDataSocket.sendMessage(data);
                        }

                        // split message into date/time and message
                        String[] parts = message.split(",");
                        String dateTime = parts[0];
                        String msg = parts[1];
                        // add date/time and message to message history
                        String[] msgWithTime = {dateTime, msg};
                        messageHistory.add(msgWithTime);

                        System.out.println("HiSTORY SELECTED:\t" + data);
                    } else if (message.trim().toUpperCase().equals("HELP")) {
                        userData.add(currentTime + "\t:\t" + message); // store the user input along with the time it was received
                        myDataSocket.sendMessage("help Available commands: HELP, STR FILTER, NUM FILTER, HISTORY, END/QUIT.");
                    } else if (message.trim().toUpperCase().equals("STR FILTER")) {
                        myDataSocket.sendMessage("Enter a string containing digits: ");
                        String str = myDataSocket.receiveMessage();
                        String result = removeDigits(str);
                        userData.add(currentTime + "\t:\t" + message + result); // store the user input along with the time it was received
                        myDataSocket.sendMessage("String with digits removed: " + result);
                    } else if (message.trim().toUpperCase().equals("NUM FILTER")) {
                        myDataSocket.sendMessage("Enter a string with digits: ");
                        String str = myDataSocket.receiveMessage();
                        String result = getDigits(str);
                        userData.add(currentTime + "\t:\t" + message + result); // store the user input along with the time it was received
                        myDataSocket.sendMessage("Digits from the string: " + result);
                    } else if (message.trim().toUpperCase().contains("end") || message.contains("quit")) {
                        myDataSocket.sendMessage("Session over.");
                        myDataSocket.close();
                        done = true;
                    } else {
                        userData.add(currentTime + ",Wrong input:," + message); // store the user input along with the time it was received
                        myDataSocket.sendMessage("error,No,such,command.,Available,commands:,HELP,STR.FILTER,NUM.FILTER,HISTORY,END/QUIT");

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