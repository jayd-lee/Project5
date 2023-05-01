import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Files {
    private String messagePath;
    private String conversationPath;

    public Files(int choice) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            if (choice == 6) {
                System.out.println("Enter the file path for message data you want to import: ");
                this.messagePath = reader.readLine();
            } else {
                System.out.println("Enter the file path for conversation data you want to export: ");
                this.conversationPath = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                if ((send.equals(sender) && recipient.equals(receive)) || send.equals(recipient) && recipient.equals(sender)) {
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