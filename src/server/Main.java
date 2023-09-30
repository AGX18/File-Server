package server;

//import client.Client;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("F:\\JavaProjects\\File Server\\File Server\\task\\src\\server\\data");
        boolean createdNewDirectory = file.mkdirs();

        Server server = new Server();

        server.connect();

        client.Main.main(args);


    }
}
