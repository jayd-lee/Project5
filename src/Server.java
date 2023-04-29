import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server implements Runnable {
    Socket socket;
    public Server(Socket socket) {
        if(socket == null) {
            System.out.println("Socket is null");
        }
        this.socket = socket;
    }

    public void run() {
        System.out.printf("connection received from %s\n", socket);
        // try {
            // socket open: make PrinterWriter and Scanner from it...
            // PrintWriter pw = new PrintWriter(socket.getOutputStream());
            // Scanner in = new Scanner(socket.getInputStream());
            // // read from input, and echo output...
            // while (in.hasNextLine()) {
            //     String line = in.nextLine();
            //     System.out.printf("%s says: %s\n", socket, line);
            //     pw.printf("echo: %s\n", line);
            //     pw.flush();
            // }
            // // input done, close connections...
            // pw.close();
            // in.close();
        // } catch (IO e) {
        //     e.printStackTrace();
        // }
    }
    public static void main(String[] args) throws IOException {
        // allocate server socket at given port...
        ServerSocket serverSocket = new ServerSocket(4343);
        System.out.printf("socket open, waiting for connections on %s\n", serverSocket);
        // infinite server loop: accept connection,
        // spawn thread to handle...
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }
    }
}
