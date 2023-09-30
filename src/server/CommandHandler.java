package server;

import java.io.*;
import java.net.Socket;

public class CommandHandler {
    Command command;

    Socket socket;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand(Socket socket) {

        command.execute(socket);
    }
}

interface Command {
    static String STORAGE = "src/server/data";

    void execute(Socket socket);

}

class getCommand implements Command {
    @Override
    public void execute(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String fileName = input.readUTF();

            File file = new File(STORAGE , fileName);

            if (file.exists() && file.isFile()) {
                // Send a success response code (e.g., "200") to the client
                output.writeUTF("200");

                // Send the file content to the client
                try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        output.writeUTF(line);
                    }
                }
            } else {
                // Send a not-found response code (e.g., "404") to the client
                output.writeUTF("404");
            }

            System.out.println("Request processed.");

        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

}







class createCommand implements Command  {
    @Override
    public void execute(Socket socket) {

        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())

        ) {
            String fileName = input.readUTF();
            File file = new File(STORAGE, fileName);

            if (file.exists()) {
                output.writeUTF("403"); // File already exists
            } else {
                output.writeUTF("200"); // File creation allowed
                output.writeUTF("Enter file content: ");

                String content = input.readUTF();

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(content);
                    output.writeUTF("201"); // File created successfully
                } catch (IOException e) {
                    output.writeUTF("403"); // Error while writing content
                }
            }
        } catch (IOException e) {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("403"); // Error during file creation
            } catch (IOException ioException) {
                // Handle exception while sending response (optional)
            }
        }
    }



}

class deleteCommand implements Command {
    @Override
    public void execute(Socket socket) {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())

        ) {
            String fileName = input.readUTF();
            File file = new File(STORAGE, fileName);

            if (file.delete()) {
                output.writeUTF("200"); // File deleted successfully
            } else {
                output.writeUTF("404"); // failed to delete file

            }

        } catch (IOException e) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("404"); // Error during file creation
            } catch (IOException ioException) {
                // Handle exception while sending response (optional)
            }
        }

    }




}
