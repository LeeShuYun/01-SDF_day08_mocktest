package Http;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//https://docs.oracle.com/en/cloud/paas/iot-cloud/iotsp/com/oracle/iot/client/message/RequestMessage.html
// main web server class
public class HttpServer {
    // Task 3
    /*--port <port number> - the port that the server will listen to. If
    this is not specified, then default to port 3000*/
    /*- --docRoot <colon delimited list of directories> -
    one or more directories where the HTML, CSS and JavaScript files
    and images are stored. If not specified, default to static directory in
    the current path.*/
    // server command defaults
    // set 
    private Integer portNum = 3000;
    private String docRootDir1 = "./target";
    private String docRootDir2 = "";
    private ServerSocket serverSoc;
    private Socket socket;

    //constructors ==============================================================================
    //takes in params to allow you to change them
    public HttpServer(Integer portNum, String docRootDir1, String docRootDir2){
        this.portNum = portNum;
        this.docRootDir1 = docRootDir1;
        this.docRootDir2 = docRootDir2;
    }

    //methods ===================================================================================
    public void start() {
        
        
        try {
            // opening a port with either default or specified port number
            //open server socket for clients
            System.out.printf("Opening Socket on port %d. \nDirectory1: %s \nDirectory2: %s \n", 
            this.portNum, this.docRootDir1, this.docRootDir2);
            this.serverSoc = new ServerSocket(this.portNum);
            // Task 4 - check for problems on port and docRoot
            // listening on port
            System.out.printf("Accepting connection on %d\n", portNum);
            this.socket = serverSoc.accept();

        } catch (IOException e) {
            System.out.println("Error when opening the socket. Server closing.");
            e.printStackTrace();
            System.exit(1);
        } catch (SecurityException e) {
            System.out.println(
                    "Security manager exists and its checkListen method doesn't allow the operation. Server closing.");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Port parameter is outside the specified range of valid port values, which is between 0 and 65535, inclusive. Server closing.");
            e.printStackTrace();
            System.exit(1);
        }

        // checking for docRoot path validity. If folder is not there then add the
        // folder, or throw the exception and exit server
        Path path1 = Paths.get(docRootDir1); // Path p = Paths.get(String <FolderName>);
        Path path2 = Paths.get(docRootDir2);
        if (!Files.isDirectory(path1)) {
            try {
                Files.createDirectory(path1);
            } catch (IOException e) {
                System.out.println("Error: IO Exception, directory does not exist." + e.getMessage());
                System.exit(1);
            }
        }
        if (!Files.isDirectory(path2)) {
            try {
                Files.createDirectory(path2);
            } catch (IOException e) {
                System.out.println("Error: IO Exception, directory does not exist." + e.getMessage());
                System.exit(1);
            }
        }

        try{
            // // task 5 - create thread pool
            ExecutorService threadPool = Executors.newFixedThreadPool(3);

            // // opening data input stream through the socket
            InputStream is = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            // infinite wait loop to pick up client msgs
            while (true) {
                String msg = dis.readUTF();
                System.out.println("Client msg received> " + msg);
                if (msg.equalsIgnoreCase("exit")){
                    System.out.println("bye bye");
                }
            }
            // // clean up
            // threadPool.shutdown();
        }catch(SecurityException se){
            se.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Closing socket and thread pool...");
        try{
            serverSoc.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
