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

        System.out.printf("New connection on port %d\n", this.socket.getPort());
        // opening data input stream through the socket
        InputStream is = this.socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        
        try {
            String line = br.readLine();
            System.out.println("Client msg received> " + line);

            // String[] values = msg.split(" ");

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

}
