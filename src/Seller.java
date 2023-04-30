import java.util.ArrayList;
import java.io.*;

public class Seller {
    private ArrayList<String> sellers = new ArrayList<String>();
    public Seller() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/seller.txt")));
            String line = br.readLine();
            while(line != null) {
                String[] details = line.split(",");
                String name = details[0].trim();
                sellers.add(name);
                line = br.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getSeller() {
        return sellers;
    }
}