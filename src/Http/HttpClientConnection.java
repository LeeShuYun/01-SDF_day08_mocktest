package Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientConnection implements Runnable {
    // handles the the request and response communication between the server and a
    // client (browser)
    private Socket socket;

    public HttpClientConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            // opening data input stream through the socket
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            System.out.printf("Client moved to port %d\n", this.socket.getPort());
        
            String line = br.readLine();
            System.out.println(line);
            System.out.println("Client msg received> " + line);

            String response = "I got your message";
            OutputStream os = this.socket.getOutputStream();
            HttpWriter httpWriter = new HttpWriter(os);
            if (line != null){
                String[] lineParseList = line.trim().split(" "); 
                if (lineParseList[0].equals("GET")){
                    httpWriter.writeString("HTTP/1.1 405 Method Not Allowed \n");
                    httpWriter.writeString("<method name> not supported \n");
                    
                }
                switch (lineParseList[0]){
                    case "GET":
                        httpWriter.writeString("HTTP/1.1 200 OK \nContent-Type: text/html \n");
                        break;
                    case "":
                        break;
                    default:
                        break;
            }
            //     response = "HTTP/1.1 405 Method Not Allowed"
            }

            if()

        } catch (Exception ex) {
            ex.printStackTrace();
        }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                     e.printStackTrace();
            
                }

    }

}
}
