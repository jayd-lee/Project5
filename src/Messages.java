import java.io.*;
import java.util.ArrayList;

public class Messages {
    private ArrayList<String> messages;
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
            for(int x = 0; x < messagesNew.size(); x++) {
                pw.println(messages.get(x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
