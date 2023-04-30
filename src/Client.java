import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client {
    static Scanner scan = new Scanner(System.in);
    private static ArrayList<String> messageList = new ArrayList<String>();
    private static ArrayList<String> storeList = new ArrayList<String>();
    private static ArrayList<String> sellerList = new ArrayList<String>();
    private static ArrayList<String> customerList = new ArrayList<String>();
    private static ArrayList<String> invisibleToList = new ArrayList<String>();

    private static ArrayList<String> invisibleByList = new ArrayList<String>();
    private static ArrayList<String> blockedList = new ArrayList<String>();

    private static ArrayList<String> blockedMeList = new ArrayList<String>();


    public static User user;
    private static boolean isSeller;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        // create socket on agreed upon port (and local host for this example)...
        Socket socket = null;
        try {
            socket = new Socket("localhost", 4343);
            System.out.println("Connection successful");
        } catch (ConnectException e) {
            System.out.println("Connection failed");
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("What is ur name?");
        String name = scan.nextLine();
        System.out.println("What is ur home address?");
        String address = scan.nextLine();
        System.out.println("What is ur social security number?");
        int ssn = scan.nextInt();
        scan.nextLine();
        // // open input stream first, gets header from server...
        // ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        // // open output stream second, send header to server...
        // ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        // oos.flush(); // ensure data is sent to the server
        // // read object(s) from server...
        // String s1 = (String) ois.readObject();
        // System.out.printf("received from server: %s\n", s1);
        // // write object(s) to server...
        // String s2 = s1.toUpperCase();
        // oos.writeObject(s2);
        // oos.flush(); // ensure data is sent to the server
        // System.out.printf("sent to server: %s\n", s2);
        // // close streams...
        // oos.close();
        // ois.close();
    }
}
