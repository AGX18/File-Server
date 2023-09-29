package src.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    CommandHandler commandHandler = new CommandHandler();
    public HashSet<String> files = new HashSet<>();

    String address = "127.0.0.1";
    int port = 23456;

    Server() {
        System.out.println("Server started!");
    }

    public String executeCommand(String command, String fileName) {
        switch (command) {
            case "get" -> {
                commandHandler.setCommand(new getCommand());

            }
            case "add" -> {
                commandHandler.setCommand(new addCommand());
            }
            case "delete" -> {
                commandHandler.setCommand(new deleteCommand());
            }
            case "exit" -> {
                //System.out.println("Server stopped!");
                System.exit(0);
            }
            default -> {
                return ("Unknown command");
            }
        }
        return commandHandler.executeCommand(fileName, files);
    }



    public void connect() {
        try(ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
//            while(true) {
                String command = input.readUTF();
                System.out.println("Received: " + command);
                String[] clientInput = command.split(" ");
                String result;
                if(command.equals("Give me everything you have!")) {
                    result = "All files were sent!";
                }
                else {
                    result = executeCommand(clientInput[0], clientInput[1]);
                }
                output.writeUTF(result);
                System.out.println("Sent: " + result);
//            }
        }
        catch(Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
