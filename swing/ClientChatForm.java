/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN-PC
 */
public class ClientChatForm extends JFrame {

    Socket con;
    JPanel panel;
    JTextField newMsg;
    JTextArea chatHistory;
    JButton send;
    DataInputStream dis;
    DataOutputStream dos;

    public ClientChatForm() {
        panel = new JPanel(null);
        newMsg = new JTextField();
        chatHistory = new JTextArea();
        send = new JButton("Send");
        this.setSize(600, 600);
        chatHistory.setBounds(25, 25, 500, 420);
        newMsg.setBounds(25, 480, 300, 50);
        send.setBounds(330, 480, 200, 50);
        send.addActionListener(new SendMsg());
        panel.add(newMsg);
        panel.add(send);
        panel.add(chatHistory);
        this.setTitle("Client");
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        try {
            con = new Socket(InetAddress.getLocalHost(), 3333);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        chatHistory.setText("Connected to server. \n");
        while (true) {

            try {
                dis = new DataInputStream(new BufferedInputStream(con.getInputStream()));
                String str = dis.readUTF();
                chatHistory.setText(chatHistory.getText() + "Server: " + str + "\n");
            } catch (IOException ex) {
                chatHistory.setText(chatHistory.getText() + "\n" + "Reading message fail. (IOException)");
                ex.printStackTrace();
//                try {
//                    Thread.sleep(3000);
//                    System.exit(0);
//                } catch (InterruptedException exe) {
//                    exe.printStackTrace();
//                }
            }

        }

    }

    private class SendMsg implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!newMsg.getText().equals("")) {
                chatHistory.setText(chatHistory.getText() + "ME: " + newMsg.getText() + "\n");
                try {
                    dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
                    dos.writeUTF(newMsg.getText());
                    dos.flush();
                } catch (IOException ex) {
                    chatHistory.setText(chatHistory.getText() + "\n" + "Reading message fail. (IOException)");
                    ex.printStackTrace();
//                    try {
//                        Thread.sleep(3000);
//                        System.exit(0);
//                    } catch (InterruptedException exe) {
//                        exe.printStackTrace();
//                    }
                }
                newMsg.setText("");
            }
        }

    }

    public static void main(String[] args) {
        ClientChatForm client = new ClientChatForm();
    }
}
