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
import java.net.ServerSocket;
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
public class ServerChatForm extends JFrame {

    static ServerSocket server;
    Socket conn;
    JPanel panel;
    JTextField newMsg;
    JTextArea chatHistory;
    JButton send;
    DataInputStream dis;
    DataOutputStream dos;
//    DataInputStream dis;
//    DataOutputStream dos;

    public ServerChatForm() {
        panel = new JPanel(null);
        newMsg = new JTextField();
        chatHistory = new JTextArea();
        send = new JButton("Send");
        this.setSize(600, 600);
        this.setTitle("server");
        chatHistory.setBounds(25, 25, 500, 420);
        newMsg.setBounds(25, 480, 300, 50);
        send.setBounds(330, 480, 200, 50);
        panel.add(newMsg);
        panel.add(send);
        panel.add(chatHistory);
        send.addActionListener(new MakeConnect());
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        try {
            server = new ServerSocket(3333, 1, InetAddress.getLocalHost());
            chatHistory.setText("Waiting for Client");
            conn = server.accept();
            chatHistory.setText(chatHistory.getText() + "\n" + "Client Found");
            while (true) {
                try {
                    dis = new DataInputStream(new BufferedInputStream(conn.getInputStream()));

                    String str = dis.readUTF();
                    chatHistory.setText(chatHistory.getText() + "\n" + "Client1: " + str);
                } catch (IOException e) {
                    chatHistory.setText(chatHistory.getText() + "\n" + "Reading message fail. (IOException)");
//                    try {
//                        Thread.sleep(3000);
//                        System.exit(0);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    class MakeConnect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if ((e.getSource() == send) && (!newMsg.getText().equals(""))) {
                chatHistory.setText(chatHistory.getText() + "\n" + "ME: " + newMsg.getText());
                try {
                    dos = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
                    dos.writeUTF(newMsg.getText());
                    dos.flush();
                } catch (IOException eio) {
                    eio.printStackTrace();
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                       ex.printStackTrace();
//                    }
//                    System.exit(0);
                }
                newMsg.setText("");
            }
        }
    }

    public static void main(String[] args) {
        ServerChatForm server = new ServerChatForm();
    }
}
