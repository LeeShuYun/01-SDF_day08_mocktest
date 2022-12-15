package Http;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        Integer portNum = 3000;
        String docRootDir1 = "./target";
        String docRootDir2 = "";
        // Task 3
        // checking for port cmds
        if (args.length > 4) {
            System.out.println(
                    "Usage: java -cp Http.Main --port <port number> --docRoot <directory 1>:<directory2>");
        } else {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("--port")) {
                    portNum = Integer.parseInt(args[i + 1]);
                    System.out.println("portNum " + portNum);
                }
                if (args[i].equalsIgnoreCase("--docRoot")) {
                    //there must always be 2 directories if you set directory
                    docRootDir2 = args[i + 1];
                    String[] tempSortDir = docRootDir2.split(":");
                    docRootDir1 = tempSortDir[0];
                    docRootDir2 = tempSortDir[1];
                }
            }
        }

        // now that we have the portnum and directories, we can start the server
        HttpServer server = new HttpServer(portNum, docRootDir1, docRootDir2);
        server.start();

    }
}