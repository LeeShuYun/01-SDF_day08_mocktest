package Http;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientConnection implements Runnable {
    // handles the the request and response communication between the server and a client (browser)
    private Socket socket;

    public HttpClientConnection(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {

        System.out.printf("New connection on port %d\n", socket.getPort());

        try {
            String payload = IOUtils.read(socket);
            System.out.println("Received>>>" + payload);

            String[] values = payload.split(" ");

            // Integer count = Integer.parseInt(values[0]);
            // Integer range = Integer.parseInt(values[1]);

            String response = "I got your message";

            IOUtils.write(socket, response);
        } catch (Exception ex) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }

    }

    public String request() {

    }
}
