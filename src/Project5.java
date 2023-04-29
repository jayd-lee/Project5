import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Project5 {
    public static void main(String[] args) {
        Socket socket = new Socket();
        Server server = new Server(socket);
        try {
            server.serverStart();
        } catch (IOException e) {
            System.out.println("Server Start IOException");
        }
        
    }

}
