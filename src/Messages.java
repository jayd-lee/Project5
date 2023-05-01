import javax.swing.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Messages {
    private ArrayList<String> messages = new ArrayList<>();
    public Messages() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("messages.txt")));
            String line = br.readLine();
            while(line != null) {
                messages.add(line);
                line = br.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getMessages() {
        return messages;
    }
    public void writeMessages(ArrayList<String> messagesNew) {
        try{
            FileWriter fw = new FileWriter(new File("messages.txt"));
            PrintWriter pw = new PrintWriter(fw);
            String messageFinal= "";
            for(String message : messagesNew) {
                System.out.println(message);
                messageFinal += message + "\n";
            }
            JOptionPane.showMessageDialog(null, messageFinal);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}