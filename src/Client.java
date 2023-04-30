import java.io.*;
import java.net.*;

// Client class
class Client {

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port number
        try (Socket socket = new Socket("localhost", 1234)) {
            // writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // reading from console
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String l = null;

            while (!"Exit".equalsIgnoreCase(l)) {

                l = br.readLine();
                out.println(l);
                out.flush();
                System.out.println("Server replied " + in.readLine());
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
