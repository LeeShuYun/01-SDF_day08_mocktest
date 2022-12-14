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
public class HttpServer {

    //main web server class
    public static void main(String[] args) throws Exception{
        public void run(){
        // Task 3
        /*--port <port number> - the port that the server will listen to. If
        this is not specified, then default to port 3000*/
        /*- --docRoot <colon delimited list of directories> -
        one or more directories where the HTML, CSS and JavaScript files
        and images are stored. If not specified, default to static directory in
        the current path.*/
        //command defaults 
        Integer portNum = 3000;
        String docRootDir1 = "./target";
        String docRootDir2 = "";
        
        //checking for port cmds
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--port")){
                portNum = Integer.parseInt(args[i+1]);
            }
            if (args[i].equalsIgnoreCase("--docRoot")){
                docRootDir2 = args[i+1];
                String[] tempSortDir = docRootDir2.split(":");
                docRootDir2 = tempSortDir[1];
            }
        }
        Socket socket;
        ServerSocket serverSoc;
        try{
            // Task 4 - check for problems on port and docRoot

            //opening a port with either default or specified port number 
            System.out.println("Opening Socket...");
            serverSoc = new ServerSocket(portNum);

            // listening on port 
            socket = serverSoc.accept(); 
            System.out.printf("Accepting connection on %d", portNum);


        }catch(IOException e){
            System.out.println("Error when opening the socket. Server closing.");
            e.printStackTrace();
            System.exit(1);
        }catch(SecurityException e){
            System.out.println("Security manager exists and its checkListen method doesn't allow the operation. Server closing.");
            e.printStackTrace();
            System.exit(1);
        }catch(IllegalArgumentException e){
            System.out.println("Port parameter is outside the specified range of valid port values, which is between 0 and 65535, inclusive. Server closing.");
            e.printStackTrace();
            System.exit(1);
        }

        //checking for docRoot path validity. If folder is not there then add the folder, or throw the exception and exit server
        Path path1 = Paths.get(docRootDir1); //Path p = Paths.get(String <FolderName>);
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

        //task 5 - create thread pool
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        // opening data input stream through the socket
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        //infinite wait loop to pick up client msgs
        while(true){
            String msg = dis.readUTF();
            System.out.println("Client msg received>" + msg);


        }
        //clean up
        thrPool.shutdown();
        System.out.println("Closing socket and thread pool...");
        serverSoc.close();
        
    }
    }
    
}
