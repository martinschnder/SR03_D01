package server;

import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String outputString = input.readLine();
                if (outputString == null) {
                    System.out.println("A client has disconnected");
                    break;
                }
                if (outputString.equals("exit")) {
                    break;
                }
                printToAllClients(outputString);
                System.out.println("Server received " + outputString);
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
}
