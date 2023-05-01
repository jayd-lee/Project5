import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class Server implements Runnable {
    Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.printf("connection received from %s\n", socket);
        GUI gui = new GUI();
        gui.startProgram();
    }


    public static void main(String[] args) throws IOException {
// allocate server socket at given port...
        ServerSocket serverSocket = new ServerSocket(9876);
        System.out.printf("socket open, waiting for connections on %s\n",
                serverSocket);
// infinite server loop: accept connection,
// spawn thread to handle...
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }
    }
}

