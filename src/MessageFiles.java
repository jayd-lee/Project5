import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Files.java
 *
 * This program implements the files selection criteria of the project by exporting and importing messages from files.
 *
 * @author Dhanush Manjunath, Aadi Gupta, Jayden Lee, Kylie Houston, LE2
 *
 * @version 4/10/23
 *
 */
public class MessageFiles {
    private String messagePath;
    private String conversationPath;

    public MessageFiles(int choice) {
        if (choice == 6) { // Import
            this.messagePath = JOptionPane.showInputDialog("Enter the file path for message data you want to import: ");
        } else if (choice == 7) { // Export
            this.conversationPath = JOptionPane.showInputDialog("Enter the file path for conversation data you want to export: ");
        }
    }

    public ArrayList<String> readMessage() {
        ArrayList<String> allMessages = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(messagePath)));
            String l;
            while ((l = br.readLine()) != null) {
                allMessages.add(l);
                //l = br.readLine();
            }
            // br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allMessages;
    }

    public void writeMessage(ArrayList<String> messagesNew) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(messagePath)))) {
            for (String message : messagesNew) {
                pw.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportConversation(ArrayList<String> conversationData, String sender, String recipient) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(conversationPath)))) {
            pw.println("Participants,Sender,Timestamp,Contents");
            for (int x = 0; x < conversationData.size(); x++) {
                String[] line = conversationData.get(x).split(",");
                String send = line[0].trim();
                String receive = line[1].trim();
                boolean one = send.equals(sender) && recipient.equals(receive);
                boolean two = send.equals(recipient) && recipient.equals(sender);
                if (one || two) {
                    pw.println(conversationData.get(x));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public ArrayList<String[]> readConversation() {
//        ArrayList<String[]> allConversations = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(new File(conversationPath)))) {
//            String line = br.readLine();
//            while (line != null) {
//                String[] conversation = line.split(",");
//                allConversations.add(conversation);
//                line = br.readLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return allConversations;
//    }

    public String importText(String sender, String recipientName) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(messagePath)))) {
            String contents = "";
            String l = br.readLine();
            while (l != null) {
                System.out.println(l);
                contents += l + "\n";
                l = br.readLine();
            }
            contents = contents.trim();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String message = sender + "," + recipientName + "," + timestamp + "," +
                    contents + "," + "false,false,false,false";
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
