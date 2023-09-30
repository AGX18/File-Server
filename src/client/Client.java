package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String address = "127.0.0.1";
    int port = 23456;

    public Client() {
//        System.out.println("client started!");
    }

    public void connect() {
        try(Socket socket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
            String command = scanner.nextLine();
            if(command.equals("exit")) {
                System.out.println("The request was sent.");
                output.writeUTF("exit");
                socket.close();
                System.exit(0);
            }
            int cmd = Integer.parseInt(command);
            switch (cmd) {
                case 1 -> {
                    get(output, input);
                }
                case 2 -> {
                    create(output, input);
                }
                case 3 -> {
                    delete(output, input);
                }
                default -> {
                    System.out.println("Unknown command");
                }
            }
            output.flush();




        }
        catch(Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }




    private void get(DataOutputStream output, DataInputStream input) {
        try {
            // Send the "get" command to the server
            output.writeUTF("get");

            // Prompt the user to enter the filename to be retrieved
            String fileName = promptFileName();
            assert fileName != null;
            output.writeUTF(fileName);

            System.out.println("The request was sent.");

            // Receive the response from the server
            String responseCode = input.readUTF();

            if (responseCode.equals("200")) {
                // File found, receive and save the content
                receiveFileContent(input);
            } else if (responseCode.equals("404")) {
                // File not found on the server
                System.out.println("The response says that the file was not found!");
            } else {
                // Unknown response code
                System.out.println("Unknown response code: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private static String promptFileName() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter filename: ");
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return null;
        }
    }

    private static void receiveFileContent(DataInputStream input )  {
        String line;
        try {
            line = input.readUTF();
            System.out.println(line);
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return;
        }


    }

    private void create(DataOutputStream output, DataInputStream input) {
        try {
            // Send the "get" command to the server
            output.writeUTF("create");

            // Prompt the user to enter the filename to be retrieved
            String fileName = promptFileName();
            output.writeUTF(fileName);

            String fileContent = promptFileContent();
            output.writeUTF(fileContent);

            System.out.println("The request was sent.");

            // Receive the response from the server
            String responseCode = input.readUTF();

            if (responseCode.equals("200")) {
                // File created
                System.out.println("The response says that file was created!");
            } else if (responseCode.equals("403")) {
                // File exists already on the server
                System.out.println("The response says that the file already exits!");
            } else {
                // Unknown response code
                System.out.println("Unknown response code: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private static String promptFileContent() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder content = new StringBuilder();
        System.out.println("Enter file content: ");
//        try {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.isEmpty()) {
//                    break; // End of content
//                }
//                content.append(line).append("\n");
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading input: " + e.getMessage());
//        }
        Scanner scanner = new Scanner(System.in);
        content = new StringBuilder(scanner.nextLine());
        return content.toString();
    }

    private void delete(DataOutputStream output, DataInputStream input) {
        try {
            // Send the "get" command to the server
            output.writeUTF("delete");

            // Prompt the user to enter the filename to be retrieved
            String fileName = promptFileName();
            assert fileName != null;
            output.writeUTF(fileName);
            System.out.println("The request was sent.");

            // Receive the response from the server
            String responseCode = input.readUTF();

            if (responseCode.equals("200")) {
                // File created
                System.out.println("The response says that the file was successfully deleted!\n");
            } else if (responseCode.equals("404")) {
                // File exists already on the server
                System.out.println("The response says that the file was not found!");
            } else {
                // Unknown response code
                System.out.println("Unknown response code: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }


}


