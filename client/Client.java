package client;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.53", 20000);
            // BufferedReader input = new BufferedReader(new
            // InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String userInput;
            // String response;
            String clientName = "empty";
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                if (clientName.equals("empty")) {
                    System.out.println("Enter youre name :");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput + " joined the group");
                    if (userInput.equals("exit")) {
                        break;
                    }
                } else {
                    String message = ("(" + clientName + ") message: ");
                    // System.out.print(message);
                    userInput = scanner.nextLine();
                    output.println(message + " " + userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                }
            } while (!userInput.equals("exit"));

            scanner.close();

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}
