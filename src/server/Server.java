package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    CommandHandler commandHandler = new CommandHandler();

    String address = "127.0.0.1";
    int port = 23456;

    Server() {
        System.out.println("Server started!");
    }

    public void executeCommand(String command, Socket socket) {
        commandHandler.setSocket(socket);

        switch (command) {
            case "get" -> { // get a file
                commandHandler.setCommand(new getCommand());

            }
            case "create" -> { // create a file
                commandHandler.setCommand(new createCommand());
            }
            case "delete" -> {
                commandHandler.setCommand(new deleteCommand());
            }
            case "exit" -> {
                //System.out.println("Server stopped!");
                System.exit(0);
            }
        }

        commandHandler.executeCommand(socket);
    }



    public void connect() {
        try(ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                String command = input.readUTF();
                if (command.equals("exit")) {
                    break;
                }
                executeCommand(command, socket);
            }
        }
        catch(Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}

