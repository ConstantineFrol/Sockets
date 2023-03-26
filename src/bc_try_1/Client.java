package bc_try_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Client {
    static final String endMessage = ".";

    public static void main(String[] args) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        try {
            System.out.println("Welcome to the client.\n" +
                    "What is the name of the server host?");
            String hostName = br.readLine();
            if (hostName.length() == 0) // if user did not enter a name
                hostName = "localhost";  //   use the default host name
            System.out.println("What is the port number of the server host?");
            String portNum = br.readLine();
            if (portNum.length() == 0)
                portNum = "4443"; // default port number
            Cl_Helper helper =
                    new Cl_Helper(hostName, portNum);
            boolean done = false;
            String input_message, echo = "";
            while (!done) {
                System.out.println("Enter a command to the server, or a single period to quit.");
                input_message = br.readLine();
                if ((input_message.trim()).equals(endMessage)) {
                    System.out.println("Goodbye...");
                    done = true;
                    helper.done();
                } else {
                    echo = helper.getEcho(input_message);


                    String[] output_message_code = echo.split(" ");
                    String output_message_text = "";

                    for (int i = 1; i < output_message_code.length; i++) {
                        output_message_text += output_message_code[i] + " ";
                    }


                    switch (output_message_code[0]) {

                        case "100":
                            System.out.println("Invalid command:\ttoo many arguments");
                            break;
                        case "101":
                            System.out.println("The output of your string without digits is:\t" + output_message_text);
                            break;
                        case "102":
                            System.out.println("The digit(s) in your input are:\t" + output_message_text);
                            break;
                        case "800":
                            String data = output_message_text;
                            data = data.replace("$", "\t\t\t").replace("*", "\n");
                            System.out.println(data);
                            break;
                        case "400":
                            System.out.println(output_message_text);
                            break;
                        case "999":
                            System.out.println(output_message_text);
                            System.out.println("Available Commands:\nSTRING <string> - output: your input without digits\n" +
                                        "DIGIT <string> - output: digits if any exists in your string\n" +
                                        "HISTORY - output: All your inputs in this session");
                            break;
                        default:
                            System.out.println("I'm afraid that command isn't recognized. Please try again.");
                            break;
                    }

                }
            } // end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } //end catch
    } //end main
} // end class
