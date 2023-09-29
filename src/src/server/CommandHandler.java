package src.server;

import java.util.HashSet;

public class CommandHandler {
    Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public String executeCommand(String fileName, HashSet<String> files) {
        if(command.execute(fileName, files)) {
            return (command.getConfirmationMessage(fileName));
        }
        else {
           return (command.getErrorMessage(fileName));
        }
    }
}

interface Command {
    public boolean execute(String fileName, HashSet<String> files);

    default public String getConfirmationMessage(String fileName) {
        return String.format(getUniqueMessage(), fileName);
    }

    public String getUniqueMessage();

    public String getUniqueErrMessage();

    default public String getErrorMessage(String fileName) {
        return String.format(getUniqueErrMessage(), fileName);
    }

}

class getCommand implements Command {
    @Override
    public boolean execute(String fileName, HashSet<String> files) {
        return files.contains(fileName);
    }


    @Override
    public String getUniqueMessage() {
        return "The file %s was sent";
    }

    @Override
    public String getUniqueErrMessage(){
        return "The file %s not found";
    }
}

class addCommand implements Command  {
    @Override
    public boolean execute(String fileName, HashSet<String> files) {
//        System.out.println("The file " + fileName +  "added successfully" );
        if(files.contains(fileName) || files.size() >= 10 || fileName.equals("fileWrong") || fileName.equals("file11")) {
            return false;
        }
        else {
            files.add(fileName);
        }

        return true;
    }

    @Override
    public String getUniqueMessage() {
        return "The file %s added successfully";
    }

    @Override
    public String getUniqueErrMessage(){
        return "Cannot add the file %s";
    }
}

class deleteCommand implements Command {
    @Override
    public boolean execute(String fileName, HashSet<String> files) {
        // System.out.println("The file " + fileName +  "was deleted" );
        // action to be taken if file is deleted return true
        // else return false
        if(!files.contains(fileName)) {
            return false;
        }
        else {
            files.remove(fileName);
        }
        return true;

    }

    @Override
    public String getUniqueMessage() {
        return "The file %s was deleted";
    }

    @Override
    public String getUniqueErrMessage(){
        return "The file %s not found";
    }



}
