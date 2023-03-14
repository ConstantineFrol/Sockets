package test_labs.Login;

import test_labs.EchoClientHelper2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Messenger extends JFrame {
    private JPanel MessagePanel;
    private JTextArea textArea;
    private JTextField textInputField;
    private JButton btnSendText;
    private JScrollPane scrollPane;
    private User user;

    private EchoClientHelper2 helper;

    private final String FILE_NAME = "users.txt";

    public Messenger() throws IOException, InterruptedException {
        setTitle("Messenger");
        setContentPane(MessagePanel);
        setMinimumSize(new Dimension(500, 500));
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        freeFile(FILE_NAME);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        btnSendText.setEnabled(false);

        boolean userProof = getProof(FILE_NAME);

        if (!userProof) {
            LoginForm loginForm = new LoginForm(this);
            System.out.println(loginForm.getTitle());

            System.out.println("After Login " + countWords(FILE_NAME));

            ConnectionForm connect = new ConnectionForm(this);
            System.out.println("Host:\t" + connect.getTextField1().getText().toString());
            System.out.println("Port:\t" + connect.getTextField2().getText().toString());

            System.out.println("After Connect " + countWords(FILE_NAME));

            dispose();

            if (countWords(FILE_NAME) == 5) {

                setLocationRelativeTo(null);
                setVisible(true);

                System.out.println("Start @ 66!");
                startProcess();

            } else dispose();
        }


        btnSendText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result, input;

                input = textInputField.getText().toString();
                displayMessage(user.getName(), input);
                textInputField.setText("");

                try {
                    result = helper.getEcho(input);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Message from Server:\t" + result);
                displayMessage("Server", result);

                if (input.toLowerCase().equals("end") || input.toLowerCase().equals("quit")) {
                    try {
                        helper.done();
                        goodbyeBeforeExit("Bye, till next time!");
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void setConnection(User user) {
        String endMessage = ".";

        try {

            EchoClientHelper2 helper = new EchoClientHelper2(user.getHost(), user.getPort());
            boolean done = false;
            String message, echo;
            while (!done) {
                System.out.println("Enter a line to receive an echo from the server");
                textArea.setText("Enter a line to receive an echo from the server");

                TimeUnit.SECONDS.sleep(10);

                textArea.setText("");

                message = textInputField.getText();
                if ((message).equals(endMessage)) {
                    done = true;
                    helper.done();
                } else {
                    echo = helper.getEcho(message);
                    textArea.setText(echo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean getProof(String filename) {
        boolean result = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("ok")) {
                    System.out.println("Yep, user ok.");
                    result = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return result;
    }

    private User readFile(String filename) {

        String data = "";

        try {
            File myObj = new File(FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An file error occurred");
            e.printStackTrace();
        }

        String file_data[] = data.split("\\s+");

        for (int i = 0; i < file_data.length; i++) {
            if (i == 2) continue;
            else {
                System.out.println("No.: " + i + "\t" + file_data[i]);
            }

        }
        user = new User();
        user.setName(file_data[0]);
        user.setPassword(file_data[1]);
        user.setHost(file_data[3]);
        user.setPort(file_data[4]);

        return user;

    }

    private void freeFile(String filename) {
        String inputFileName = filename;
        String outputFileName = "output.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName)); BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                writer.write(words[0] + " " + words[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        renameFileProcess();
    }

    private void renameFileProcess() {
        File old_file = new File(FILE_NAME);
        old_file.delete();
        File file = new File("output.txt");
        File rename = new File(FILE_NAME);
        boolean flag = file.renameTo(rename);
        if (flag == true) {
            System.out.println("File Renamed");
        } else System.out.println("Operation Rename Failed");
    }

    private void startProcess() throws IOException, InterruptedException {


//        Get to know user
        user = readFile(FILE_NAME);
        System.out.println(user.toString());

//        Create a Connection With The Server
        createConnection(user.getHost(), user.getPort());

//        Rewrite File "users.txt"
        freeFile(FILE_NAME);

//        Display message to the user
        displayMessage("", "Hello " + user.getName() + " !!!\n\n" + "Available commands:\nHELP - display commands,\nSTR FILTER - return a string,\nNUM FILTER - return digits,\nHISTORY - return all inputs, \nEND/QUIT - disconnect from a server.");

        TimeUnit.SECONDS.sleep(8);

        textArea.setText("");

        textInputField.setText("Input your choice here");
        textInputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textInputField.setText("");
                btnSendText.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });


    }

    private void createConnection(String host, String port) throws InterruptedException {

        String connection_message = "\n" + user.getName() + " - You are connected";
        try {
            helper = new EchoClientHelper2(host, port);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (helper == null) {
                System.out.println("No connection");

                goodbyeBeforeExit("NO CONNECTION !!!\n\na)\tCheck Server\nb)\tHost and Port number was incorrect");

            } else {
                textArea.replaceSelection(connection_message);
                System.out.println("Hello:\t" + connection_message);
            }
        }
    }

    // TODO FIX HISTORY DISPLAY MESSAGE & WRONG INPUT MESSAGE
    private void goodbyeBeforeExit(String text) throws InterruptedException {
        textArea.setText("");
        displayMessage("System", text);
        TimeUnit.SECONDS.sleep(4);
        for (int i = 3; i > 0; i--) {
            textArea.setText("");
            textArea.setText("EXIT IN " + i);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dispose();
        System.exit(0);
    }


    private static String prettifyString(String input) {
        String[] words = input.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            sb.append(words[i]);
            if (i < words.length - 1) {
                if (words[i].equals(",")) sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void displayMessage(String actor, String text) {

        String result = "";
        String message = text;
        textArea.setEditable(true);

        if (wordMatch("error", text) == true) {
            result = prettifyString(text);
            message = result;
            System.out.println("Error message to be displayed\n" + result);
        } else if (wordMatch("help", text) == true) {
            result = prettifyString(text);
            message = result;
            System.out.println("Help message to be displayed\n" + result);
        } else if (wordMatch("history", text) == true) {
            result = prettifyString(text);
            message = result;
            System.out.println("History message to be displayed\n" + result);
        }

        if (actor == "") {
            textArea.replaceSelection("> " + message + "\n");
        } else textArea.replaceSelection(actor + "> " + message + "\n");

        textArea.setEditable(false);
    }

    private static boolean wordMatch(String keyword, String sentence) {
        String[] words = sentence.split("\\s+");
        for (String word : words) {
            if (keyword.equals(word)) {
                System.out.println("Keyword:\t" + keyword + " Detected in:\t" + sentence);
                return true;
            }
        }
        System.out.println("Keyword:\t" + keyword + " Is NOT Detected in:\t" + sentence);
        return false;
    }

    private static int countWords(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int count = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split("\\s+");
            count += words.length;
        }
        scanner.close();
        System.out.println("Number of words: " + count);
        return count;
    }

    private static String getNumbersFromString(String inputString) {
        String result = "";
        if (inputString != null && !inputString.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inputString.length(); i++) {
                char c = inputString.charAt(i);
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            result = sb.toString();
        }
        return result;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Messenger messanger = new Messenger();


    }
}
