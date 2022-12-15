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
    private String[] directories;

    public HttpClientConnection(Socket socket, String[] filePaths) {
        this.socket = socket;
        this.directories = filePaths;
    }

    @Override
    public void run() {

        try {
            // opening data input stream through the socket
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            System.out.printf("Client moved to port %d\n", this.socket.getPort());

            //reading the request
            String line = br.readLine();
            System.out.println("Client msg received> " + line);

            // String response = "I got your message";
            //prep work for outputting later
            OutputStream os = this.socket.getOutputStream();
            HttpWriter httpWriter = new HttpWriter(os);

            
            //reading as long as there's a request. 
            //Parsing request
            if (line != null) {
                String[] lineParseList = line.trim().split(" "); //splits GET / HTTP/1.1 into list
                String command = lineParseList[0];
                String siteFileName = lineParseList[1];
                
                // checking if first word is a GET, if it's not reject everything
                if (!command.equalsIgnoreCase("GET")) {
                    httpWriter.writeString("HTTP/1.1 405 Method Not Allowed \n");
                    httpWriter.writeString("");
                    httpWriter.writeString(command + " not supported \n");
                }
                // replacing the / with /index.html
                if (siteFileName.equals("/") || siteFileName.equals("/index.html")) {
                    siteFileName = "index.html";
                    System.out.println("The site file name is:" + siteFileName);
                }
                // CASE: when file doesn't exist
                // checking all directories for the html
                Path path = Paths.get(folder, resource);
                File file = path.toFile();
                for (String filePath : this.directories) {
                    String fullPath = filePath + siteFileName;
                    System.out.println(fullPath);
                    File file = new File(fullPath);
                    Path path = Paths.get(fullPath);
                    //checking if it exists
                    if (Files.isDirectory(path)) { //try is Exists
                        // when file exists
                        httpWriter.writeString("HTTP/1.1 200 OK \nContent-Type: text/html");
                        httpWriter.writeString("Content-Size: ");
                        httpWriter.writeString("");
                        httpWriter.writeString(lineParseList[1] + " not found");
                        // File siteFile = new File(htmlFile);
                        // httpWriter.writeBytes(siteFile);
                    } else {
                        httpWriter.writeString("HTTP/1.1 404 Not Found");
                        httpWriter.writeString("");
                        httpWriter.writeString(lineParseList[1] + " not found");
                        // close socket
                        socket.close();
                    }
                }
                
                // if (image){
                    // sending images
                // File img = new File(imgFile);
                // FileInputStream image = new FileInputStream(img);
                // httpWriter.writeString("Content-Type: image/png");
                // httpWriter.writeBytes(fis);
                // }

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
