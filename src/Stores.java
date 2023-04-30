import java.io.*;
import java.util.ArrayList;

public class Stores {
    private ArrayList<String> stores;
    public Stores(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("stores.txt")));
            String l;
            while((l = br.readLine()) != null) {
                stores.add(l);
                l = br.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getStores() {
        return stores;
    }
    public void writeStores(ArrayList<String> storesNew) {
        try{
            FileWriter fw = new FileWriter(new File("stores.txt"));
            PrintWriter pw = new PrintWriter(fw);
            for(int x = 0; x < storesNew.size(); x++) {
                pw.println(storesNew.get(x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}