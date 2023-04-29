import java.io.*;
import java.net.*;
import java.util.*;

// Server class
class Server {
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting client request
            while (true) {
                Socket client = server.accept();

                // Displaying that new client is connected to server
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                // create a new thread object
                Thread t = new Thread(new ClientHandler(client));

                // This thread will handle the client
                // separately
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private static List<Socket> clients = new ArrayList<>();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                // add this client to the list of connected clients
                clients.add(clientSocket);

                // get the outputstream of client
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // get the inputstream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.printf(" Sent from the client: %s\n", line);

                    // broadcast the input to all connected clients
                    for (Socket socket : clients) {
                        PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
                        socketOut.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                        // remove this client from the list of connected clients
                        clients.remove(clientSocket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
