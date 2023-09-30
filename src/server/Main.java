package server;

//import client.Client;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("src/server/data");
        if(!file.exists()) {
            boolean createdNewDirectory = file.mkdirs();
        }

        Server server = new Server();

        server.connect();

        client.Main.main(args);


    }
}
