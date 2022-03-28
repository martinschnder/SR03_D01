package client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.53", 20000);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String userInput = "hello";
            String clientName = "empty";
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                if (clientName.equals("empty")) {
                    output.println("I want to change my name");
                    clientName = "notempty";
                } else {
                    userInput = scanner.nextLine();
                    if (userInput.equals("exit")) {
                        System.out.println("You have been disconnected.");
                        System.exit(0);
                    }
                    output.println(userInput);
                }
            } while (!userInput.equals("exit"));

            scanner.close();

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}
