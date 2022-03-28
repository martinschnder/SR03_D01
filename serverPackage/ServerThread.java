package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Thread of the server
 * Intercept messages send in the socket
 * 
 * @author Martin Schneider
 * @author Océane Bordeau
 */
public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    /**
     * Default ServerThread constructor
     * 
     * @param socket     a socket connected with the client
     * @param threadList list of the thread created
     * @throws IOException failure or interrupted I/O operations
     */
    public ServerThread(Socket socket, ArrayList<ServerThread> threadList) throws IOException {
        setSocket(socket);
        setThreadList(threadList);
        setOutput(new PrintWriter(socket.getOutputStream(), true));
    }

    /**
     * @return the current socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return the current list of threads
     */
    public ArrayList<ServerThread> getThreadList() {
        return threadList;
    }

    /**
     * @return the current output of the server
     */
    public PrintWriter getOutput() {
        return output;
    }

    /**
     * @param socket set the socket for the current class
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @param threadList set the list of threads for the current class
     */
    public void setThreadList(ArrayList<ServerThread> threadList) {
        this.threadList = threadList;
    }

    /**
     * @param output set the output for the current class
     */
    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    /**
     * <strong>Processing of the thread when it starts</strong>
     * Disconnect the socket if the input is "exit"
     * Change the thread name if the input is "I want to change my name"
     * Otherwise, print the input of the socket to all the others sockets
     * 
     * @exception IOException failure or interrupted I/O operations
     */
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

    /**
     * Print the string to all but the current socket
     * 
     * @param outputString string to be printed
     */
    private void printToAllClients(String outputString) {
        for (ServerThread sT : getThreadList()) {
            if (sT != this) {
                sT.output.println(outputString);
            }
        }
    }

    /**
     * Change the name of the current thread
     * Check if the name is not already use
     * 
     * @param name integer variable set to 0 if the client has no name already, and
     *             more than 0 if he has already one
     * @exception Exception any error occured in the function
     */
    private void changeName(Integer name) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            String userInput;
            String clientName = "";
            Boolean noClientName = true;
            while (noClientName) {
                /**
                 * while the client has not change his name
                 */
                getOutput().println("Enter your name :");
                userInput = input.readLine();
                clientName = userInput;
                noClientName = false;
                for (ServerThread sT : getThreadList()) {
                    /**
                     * check all the thread's name in the thread's list
                     */
                    if (sT.getName().equals(clientName)) {
                        /**
                         * exit from the for loop if the name already use, to start again the while loop
                         */
                        noClientName = true;
                        break;
                    }
                }
                if (!noClientName) {
                    /**
                     * the name is not already use, we change the thread's name
                     */
                    if (name > 0) {
                        /**
                         * not the first change of name
                         */
                        String outputString = getName() + " has changed his name to " + clientName;
                        printToAllClients(outputString);
                        setName(clientName);
                    } else {
                        /**
                         * first change of name
                         */
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
