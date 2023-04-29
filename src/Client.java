import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        // create socket on agreed upon port (and local host for this example)...
        Client client = new Client();
        Socket socket = null;
        try {
            socket = new Socket("", 4343);
        } catch (ConnectException e) {
            System.out.println("Connection failed");
        }
        Server server = new Server(socket);
        boolean serverstatus = client.serverStatus();
        if (!serverstatus) {
            server.serverStart();
        }
        // open input stream first, gets header from server...
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        // open output stream second, send header to server...
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush(); // ensure data is sent to the server
        // read object(s) from server...
        String s1 = (String) ois.readObject();
        System.out.printf("received from server: %s\n", s1);
        // write object(s) to server...
        String s2 = s1.toUpperCase();
        oos.writeObject(s2);
        oos.flush(); // ensure data is sent to the server
        System.out.printf("sent to server: %s\n", s2);
        // close streams...
        oos.close();
        ois.close();
    }
    public boolean serverStatus() {
        try (Socket socket = new Socket("dmanjun@purdue.edu", 4343)) {
            return true;
        } catch (IOException e) {
            System.out.println("Server Offline");
        }
        return false;
    }
}
