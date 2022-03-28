package server;

import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
// import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threadList) throws IOException {
        this.socket = socket;
        this.threadList = threadList;
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            while (true) {
                String outputString = input.readLine();
                if (outputString == null) {
                    System.out.println(this.getName() + " has disconnected");
                    break;
                }
                if (outputString.equals("exit")) {
                    break;
                }
                if (outputString.equals("I want to change my name")) {
                    changeName();
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
        for (ServerThread sT : threadList) {
            if (sT != this) {
                sT.output.println(outputString);
            }
        }
    }

    private void changeName() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String userInput;
            String clientName = "empty";
            while (clientName.equals("empty")) {
                this.output.println("Enter your name :");
                userInput = input.readLine();
                clientName = userInput;
                for (ServerThread sT : threadList) {
                    if (sT.getName().equals(clientName)) {
                        clientName = "empty";
                        break;
                    }
                }
                if (!clientName.equals("empty")) {
                    this.setName(clientName);
                    this.output.println("-------------------------");
                    String outputString = this.getName() + " has join the chat";
                    printToAllClients(outputString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
