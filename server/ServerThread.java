package server;

import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threadList) throws IOException {
        setSocket(socket);
        setThreadList(threadList);
        setOutput(new PrintWriter(socket.getOutputStream(), true));
    }

    public Socket getSocket() {
        return socket;
    }

    public ArrayList<ServerThread> getThreadList() {
        return threadList;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setThreadList(ArrayList<ServerThread> threadList) {
        this.threadList = threadList;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            Integer name = 0;
            while (true) {
                String outputString = input.readLine();
                if (outputString == null) {
                    System.out.println(getName() + " has disconnected");
                    break;
                }
                if (outputString.equals("exit")) {
                    break;
                }
                if (outputString.equals("I want to change my name")) {
                    changeName(name);
                    name++;
                } else {
                    outputString = getName() + " said : " + outputString;
                    printToAllClients(outputString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printToAllClients(String outputString) {
        for (ServerThread sT : getThreadList()) {
            if (sT != this) {
                sT.output.println(outputString);
            }
        }
    }

    private void changeName(Integer name) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            String userInput;
            String clientName = "empty";
            while (clientName.equals("empty")) {
                getOutput().println("Enter your name :");
                userInput = input.readLine();
                clientName = userInput;
                for (ServerThread sT : getThreadList()) {
                    if (sT.getName().equals(clientName)) {
                        clientName = "empty";
                        break;
                    }
                }
                if (!clientName.equals("empty")) {
                    if (name > 0) {
                        String outputString = getName() + " has changed his name to " + clientName;
                        printToAllClients(outputString);
                        setName(clientName);
                    } else {
                        setName(clientName);
                        getOutput().println("-------------------------");
                        String outputString = getName() + " has join the chat";
                        printToAllClients(outputString);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
