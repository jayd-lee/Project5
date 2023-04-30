import java.io.*;
import java.net.*;
import java.util.*;

// Server class
class Server {
    private static final List<ClientHandler> clients = new LinkedList<>();
    private static int clientCount = 1;

    public static void main(String[] args) {
        ServerSocket s = null;

        try {
            // server is listening on port 1234
            s = new ServerSocket(1234);
            s.setReuseAddress(true);

            // running infinite loop for getting client request
            while (true) {
                Socket client = s.accept();

                // Displaying that new client is connected to server
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                // create a new thread to handle the client
                ClientHandler ch = new ClientHandler(client);
                clients.add(ch);
                ch.start();
                clientCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final PrintWriter out;
        private final BufferedReader in;
        private final int clientId;

        // messageHistory stores all messages sent by all clients
        private static final LinkedList<String> messageHistory = new LinkedList<>();

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientId = clientCount;
        }

        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.printf("Client %d sent: %s\n", clientId, line);
                    // add message to messageHistory
                    messageHistory.add("Client " + clientId + ": " + line);
                    // broadcast message to all clients except the original sender
                    synchronized (clients) {
                        for (ClientHandler handler : clients) {
                            if (handler.clientId != clientId) {
                                handler.out.printf("Client %d: %s\n", clientId, line);
                            }
                        }
                    }
                    // send repeated message to original sender only
                    int count = 0;
                    for (String msg : messageHistory) {
                        if (msg.equals("Client " + clientId + ": " + line)) {
                            count++;
                        }
                    }
                    if (count > 1) {
                        out.printf("Client %d: %s\n", clientId, line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    in.close();
                    clientSocket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
