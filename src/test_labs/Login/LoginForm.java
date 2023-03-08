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

public class LoginForm extends JDialog {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(800, 800));
        setSize(getMinimumSize());
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userName = textField1.getText();
                String userPassword = String.valueOf(passwordField1.getPassword());

                user = getAuthenticatedUser(userName, userPassword);

                if (user != null) {
                    dispose();
                    try {
                        FileWriter writer = new FileWriter("users.txt", true);
                        String str = " ok";
                        writer.write(str);
                        writer.close();
                    } catch (IOException ex) {
                        System.out.println("An error occurred while writing to the file.");
                        ex.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid", "Try Again", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

//        cancelButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//            }
//        });
//
//        setVisible(true);
//    }
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    public User user;

    private User getUserAuth(String name, String password) {

        if ((name.equals("admin")) && (password.equals("qwerty"))) {
            user = new User();
            user.setName(name);
            user.setPassword(password);
            return user;
        }

        return null;
    }

    private User getAuthenticatedUser(String name, String password) {
        String data = "";

        try {
            File myObj = new File("users.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An file error occurred");
            e.printStackTrace();
        }

        String user_inputs[] = data.split(" ", 2);

        String user_name = user_inputs[0];
        String user_password = user_inputs[1];

        if ((name.equals(user_name)) && (password.equals(user_password))) {

            System.out.println("input:\t" + name + "\t" + password);
            System.out.println("Origin:\t" + user_name + "\t" + user_password);
            user = new User();
            user.setName(name);
            user.setPassword(password);

            return user;
        }

        return null;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if (user != null) {
            System.out.println("Successful Authentication of: \n" + "User: " + user.getName() + "\n");
        } else {
            System.out.println("Authentication canceled");
        }


    }

}
