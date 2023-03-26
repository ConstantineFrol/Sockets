package bc_try_1;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class Ser_Thread implements Runnable {
    static final String endMessage = ".";

    SSSocket myDataSocket;

    Ser_Thread(SSSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    List<String> messages;

    public void run() {
        boolean done = false;
        String message_from_client;
        String[] parts;
        messages = new ArrayList<>();
        messages.add("800 |$user_input$|$server_output$|*");

        try {
            while (!done) {

                message_from_client = myDataSocket.receiveMessage();
                /**/

                if (message_from_client == null) {
                    myDataSocket.sendMessage("400 You have been disconnected.");
                    System.out.println("Client disconnected....");
                    myDataSocket.close();
                    done = true;

                } //end if

                else {

                    System.out.println("message received: " + message_from_client);

                    parts = message_from_client.split(" ");


                    if (parts.length > 2) {
                        myDataSocket.sendMessage("100 Too many arguments");

                    } //end if

                    else {

                        if ((parts[0].trim()).equals(endMessage)) {
                            //Session over; close the data socket.
                            /**/
                            System.out.println("Session over.");
                            myDataSocket.close();
                            done = true;
                        } //end if

                        else if (parts[0].trim().toUpperCase().equals("STRING")) {
                            String str = parts[1];
                            String result = removeDigits(str);
                            String inputOutput = "|$" + str + "$|$" + result + "$|*";
                            messages.add(inputOutput);
                            myDataSocket.sendMessage("101 " + result);
                            System.out.println("Result: " + result);

                        } //end if else

                        else if (parts[0].trim().toUpperCase().equals("DIGIT")) {
                            String str = parts[1];
                            String result = getDigits(str);
                            String inputOutput = "|$" + str + "$|$" + result + "$|*";

                            messages.add(inputOutput);
                            myDataSocket.sendMessage("102 " + result);
                            System.out.println("Result: " + result);

                        } //end if else

                        else if (parts[0].trim().toUpperCase().equals("HISTORY")) {
                            String allMessages = String.join("", messages);
                            myDataSocket.sendMessage(allMessages);
                            System.out.println("Messages: \n" + messages);

                        } //end if else

                        else {
                            myDataSocket.sendMessage("999 Invalid command");

                        } //end else

                    } //end else

                } //end else

            } //end while !done
        } // end try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // end catch
    } //end run

    private String removeDigits(@NotNull String str) {

        StringBuilder result = new StringBuilder();

        for (char c : str.toCharArray()) {

            if (!Character.isDigit(c)) {
                result.append(c);
            } //end if

        }//end for

        return result.toString();
    }

    private String getDigits(@NotNull String str) {

        StringBuilder result = new StringBuilder();

        for (char c : str.toCharArray()) {

            if (Character.isDigit(c)) {
                result.append(c);
            } //end if

        } //end for

        return result.toString();
    }

} //end class