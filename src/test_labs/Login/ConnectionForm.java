package test_labs.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConnectionForm extends JDialog {
    private JPanel ConnectionForm;
    private JButton returnButton;
    private JButton connectButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel ButtonsPanel;
    private JPanel ConnectPanel;


    public ConnectionForm(JFrame parent) {
        super(parent);
        setTitle("Create Connection");
        setContentPane(ConnectionForm);
        setMinimumSize(new Dimension(800, 800));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostName = textField1.getText();
                String portNum = textField2.getText();


                if ((!hostName.isEmpty()) && (!portNum.isEmpty())) {

                    if (isNumeric(portNum) == true) {
                        try {
                            int res = countWords("users.txt");
                            if (res == 3) {
                                try {
                                    FileWriter writer = new FileWriter("users.txt", true);
                                    String str = " " + hostName + " " + portNum;
                                    writer.write(str);
                                    writer.close();
                                } catch (IOException ex) {
                                    System.out.println("An error occurred while writing to the file.");
                                    ex.printStackTrace();
                                }
                            }
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }

                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(ConnectionForm.this,
                                "Port number: must use numbers.", "Try Again", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(ConnectionForm.this,
                            "Fields cannot be empty.", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);

    }

    public static int countWords(String filename) throws FileNotFoundException {
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

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public static void main(String[] args) {
        ConnectionForm connection_form = new ConnectionForm(null);
    }
}
