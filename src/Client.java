import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        // create socket on agreed upon port (and local host for this example)...
        Socket socket = new Socket("localhost", 9876);

    }
}
