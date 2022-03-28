package serverPackage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server of the chat
 * 
 * @author Martin Schneider
 * @author Océane Bordeau
 */
public class Server {

    /**
     * Our main server method, waiting for new clients and starting a server thread
     * 
     * @param args command line arguments
     * @exception Exception any error occured in main
     */
    public static void main(String[] args) {
        ArrayList<ServerThread> threadList = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(20000);
            while (true) {
                System.out.println("Waiting for new client...");
                Socket socket = serverSocket.accept();
                System.out.println("A new client has entered.");
                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();
            }

        } catch (Exception e) {
            System.out.println("Error occured in main: " + e.getStackTrace());
        }
    }
}