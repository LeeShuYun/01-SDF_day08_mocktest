package Http;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        // now that we have the portnum and directories, we can start the server
        HttpServer server = new HttpServer(args);
        server.start();

    }
}