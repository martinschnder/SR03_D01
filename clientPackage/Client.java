package clientPackage;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * main Client class
 * 
 * @author Océane Bordeau
 * @author Martin Schneider
 */
public class Client {

    /**
     * extension of Thread class
     * print the messages received from the server
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.53", 20000);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String userInput = "";
            Boolean noClientName = true;
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                /**
                 * if client has no name, it sends a request to the server to change its name
                 */
                if (noClientName == true) {
                    output.println("I want to change my name");
                    noClientName = false;
                } else {
                    /**
                     * scanning the user input and sending it to the server
                     */
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
