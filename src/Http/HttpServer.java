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
public class HttpServer{
    // Task 3
    /*--port <port number> - the port that the server will listen to. If
    this is not specified, then default to port 3000*/
    /*- --docRoot <colon delimited list of directories> -
    one or more directories where the HTML, CSS and JavaScript files
    and images are stored. If not specified, default to static directory in
    the current path.*/
    // server command defaults
    private Integer portNum = 3000;
    private String[] directoryList = {".\\static\\"};
    private ServerSocket serverSoc;
    private Socket socket;
    ExecutorService threadPool;

    //constructors ==============================================================================
    //takes in the commands and processes it
    public HttpServer(String[] args){
        // Task 3
        // checking for port cmds
        if (args.length > 4) {
            System.out.println(
                "Usage: java -cp Http.Main --port <port number> --docRoot <directory 1>:<directory2>");
            } else {
                //process the port and docRoot
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equalsIgnoreCase("--port")) {
                        portNum = Integer.parseInt(args[i + 1]);
                        // System.out.println("portNumber: " + portNum);
                    }
                    if (args[i].equalsIgnoreCase("--docRoot")) {
                        this.directoryList = args[i+1].split(":");
                        // System.out.println("Directories input:" + args[i+1]);
                    }
            }
                       
        }
        // checking for docRoot path validity. Processing each path in the path list
        for (int i = 0; i < directoryList.length; i++) {
            System.out.println(directoryList[i]);
            Path path = Paths.get(directoryList[i]); // Path p = Paths.get(String <FolderName>);
            if (!Files.isDirectory(path)) {
                try {
                    Files.createDirectory(path);
                    System.out.printf("File path %s created.\n", directoryList[i]);
                } catch (IOException e) {
                    System.out.println("Error: IO Exception, directory does not exist: " + e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    //methods ===================================================================================
    public void start() {
        
        // Task 4 - check for problems on port and docRoot
        try {
            // task 5 - create thread pool
            this.threadPool = Executors.newFixedThreadPool(3);
            System.out.println("Thread pool created.");

            // opening a port with either default or specified port number
            //open server socket for clients
            System.out.printf("Opening Socket on port %d.\n", this.portNum);
            this.serverSoc = new ServerSocket(this.portNum);
            
            // infinite wait loop to listen and pick up clients
            while (true){
                // listening on port
                System.out.printf("Accepting connections on port %d.\n", portNum);
                this.socket = serverSoc.accept();

                //creates a new connection for client when socket received client
                HttpClientConnection client = new HttpClientConnection(socket);
                System.out.println("Client connected on " + this.socket.getLocalPort());
                //add client to scheduler to be managed
                this.threadPool.submit(client);
        
            }
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

        // System.out.println("Closing socket and thread pool...");
        // try{
        //     serverSoc.close();
        // }catch (IOException e){
        //     e.printStackTrace();
        //     System.out.println(e.getMessage());
        // }

    }

    public Integer getPortNum() {
        return portNum;
    }

    public String[] getDirectoryList() {
        return directoryList;
    }

    public ServerSocket getServerSoc() {
        return serverSoc;
    }

    public Socket getSocket() {
        return socket;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    
}
