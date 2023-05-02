import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.*;

public class ChatClientGUI {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private JScrollPane scrollPane;

    public ChatClientGUI(String host, int port) {
        try {
            // create socket and input/output streams
            socket = new Socket(host, port);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread readerThread = new Thread(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.startProgram();


//                try {
//                    while (true) {
//                        String message = reader.readLine();
//                        if (message == null) {
//                            break;
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
        readerThread.start();
    }


    private void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        int port = 12345;
        String host = "localhost";
        new ChatClientGUI(host, port);
    }
}