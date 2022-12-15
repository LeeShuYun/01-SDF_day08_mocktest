package Http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            System.out.println("Client msg received> " + line);

            // String response = "I got your message";
            String htmlFile = "";
            OutputStream os = this.socket.getOutputStream();
            HttpWriter httpWriter = new HttpWriter(os);
            if (line != null) {
                String[] lineParseList = line.trim().split(" ");

                //checking if it's a GET
                if (!lineParseList[0].equals("GET")) {
                    httpWriter.writeString("HTTP/1.1 405 Method Not Allowed \n");

                    httpWriter.writeString("");
                    httpWriter.writeString(lineParseList[0] + " not supported \n");
                }
                //replacing the / with /index.html
                if (lineParseList[1].equals("/") || lineParseList[1].equals("/index.html")){
                    lineParseList[1] = "index.html";
                }
                //when file doesn't exist
                Path path = Paths.get(lineParseList[1]);                
                if (!Files.isDirectory(path)){
                    httpWriter.writeString("HTTP/1.1 404 Not Found");
                    httpWriter.writeString("");
                    httpWriter.writeString(lineParseList[1] + " not found");
                    //close socket
                    socket.close();
                }else{
                    //when file exists
                    httpWriter.writeString("HTTP/1.1 200 OK");
                    httpWriter.writeString("Content-Type: text/html");
                    httpWriter.writeString(lineParseList[1] + " not found");
                    File siteFile = new File(htmlFile);
                    httpWriter.writeBytes(siteFile);
                }

                if (image){
                    //sending images
                    // File img = new File(imgFile);
                    // FileInputStream image = new FileInputStream(img);
                    httpWriter.writeString("Content-Type: image/png");
                    // httpWriter.writeBytes(fis);
                }


            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }
}
