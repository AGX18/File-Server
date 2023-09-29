package src.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String address = "127.0.0.1";
    int port = 23456;

    public Client() {
        System.out.println("client started!");
    }

    public void connect() {
        try(Socket socket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            Scanner scanner = new Scanner(System.in);
//            while(true) {
                //String command = scanner.nextLine();
                String command = "Give me everything you have!";
                System.out.println("Sent: " + command);

                output.writeUTF(command);
                output.flush();

                String response = input.readUTF();
                System.out.println("Received: " + response);
//            }
        }
        catch(Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }
}
