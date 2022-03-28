package clientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket socket;
    private BufferedReader input;

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

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
        } finally {
            try {
                getInput().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
