import java.util.ArrayList;
import java.io.*;

public class Customer {
    private ArrayList<String> customers = new ArrayList<String>();
    public Customer() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/customer.txt")));
            String line = br.readLine();
            while(line != null) {
                String[] details = line.split(",");
                String name = details[0].trim();
                customers.add(name);
                line = br.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getCustomers() {
        return customers;
    }
}
