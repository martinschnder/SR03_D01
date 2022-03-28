package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * extension of Thread class
 * print the messages received from the server
 * 
 * @author Océane Bordeau
 * @author Martin Schneider
 */
public class ClientThread extends Thread {

    private Socket socket;
    private BufferedReader input;

    /**
     * returns the socket of the current class
     * 
     * @return Socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * returns the input of the current class
     * 
     * @return BufferedReader
     */
    public BufferedReader getInput() {
        return input;
    }

    /**
     * sets the socket for the current class
     * 
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * sets the input for the current class
     * 
     * @param input the input linked to the socket
     */
    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public ClientThread(Socket socket) throws IOException {
        setSocket(socket);
        setInput(new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
    }

    @Override
    public void run() {
        try {
            /**
             * loop while listening to server incoming messages
             * print the message to the client
             */
            while (true) {
                String response = getInput().readLine();
                if (response == null) {
                    System.out.println("Server has disconnected");
                    System.exit(0);
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
