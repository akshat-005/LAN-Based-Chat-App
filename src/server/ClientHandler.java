package server;

import common.Message;
import common.ConsoleColors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * ClientHandler - Handles communication with a single client
 * 
 * This class demonstrates INHERITANCE and POLYMORPHISM by extending Thread.
 * Each instance runs on a separate thread to handle one client connection.
 * 
 * OOP Principles Demonstrated:
 * - Inheritance: Extends Thread class
 * - Polymorphism: Overrides run() method
 * - Encapsulation: Private fields and methods
 * - Single Responsibility: Handles one client only
 * 
 * Multithreading:
 * - Each client runs on its own thread
 * - Non-blocking communication
 * - Independent of other clients
 */
public class ClientHandler extends Thread {

    // Client connection and I/O streams
    private final Socket clientSocket;
    private final ChatServer server;
    private PrintWriter out;
    private BufferedReader in;

    // Client information
    private String username;
    private volatile boolean running;

    /**
     * Constructor - Creates a handler for a client connection
     * 
     * @param clientSocket The socket connected to the client
     * @param server       Reference to the main server
     */
    public ClientHandler(Socket clientSocket, ChatServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.running = true;
    }

    /**
     * run() method - Overrides Thread.run() (POLYMORPHISM)
     * 
     * This method:
     * 1. Sets up I/O streams
     * 2. Receives client username
     * 3. Announces client join
     * 4. Listens for messages in a loop
     * 5. Broadcasts messages to other clients
     * 6. Handles disconnection
     */
    @Override
    public void run() {
        try {
            // Set up I/O streams
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Request username from client
            out.println("ENTER_USERNAME");
            username = in.readLine();

            // Validate username
            if (username == null || username.trim().isEmpty()) {
                out.println("ERROR: Invalid username. Connection closed.");
                closeConnection();
                return;
            }

            username = username.trim();

            // Send welcome message to client
            out.println("SUCCESS: Connected to chat server as " + username);

            // Announce new user to all clients
            String joinMessage = Message.createSystemMessage(
                    username + " has joined the chat!");
            server.broadcastSystemMessage(joinMessage);

            System.out.println(ConsoleColors.successMessage("User '" + username + "' joined the chat"));

            // Main message loop - listen for client messages
            String messageContent;
            while (running && (messageContent = in.readLine()) != null) {

                // Check for exit command
                if (messageContent.equalsIgnoreCase("exit")) {
                    break;
                }

                // Validate message
                if (!Message.isValidMessage(messageContent)) {
                    out.println("ERROR: Cannot send empty message");
                    continue;
                }

                try {
                    // Create message object
                    Message message = new Message(username, messageContent);

                    // Log message on server
                    System.out.println(message.getFormattedMessage());

                    // Broadcast to all other clients
                    server.broadcastMessage(message.getFormattedMessage(), this);

                    // Echo back to sender for confirmation
                    out.println(message.getFormattedMessage());

                } catch (IllegalArgumentException e) {
                    // Invalid message format
                    out.println("ERROR: " + e.getMessage());
                }
            }

        } catch (SocketException e) {
            // Client disconnected abruptly
            if (running) {
                System.err.println("Client '" + username + "' disconnected abruptly: " +
                        e.getMessage());
            }

        } catch (IOException e) {
            // I/O error during communication
            if (running) {
                System.err.println("I/O error with client '" + username + "': " +
                        e.getMessage());
            }

        } catch (Exception e) {
            // Unexpected error
            System.err.println("Unexpected error with client '" + username + "': " +
                    e.getMessage());
            e.printStackTrace();

        } finally {
            // Clean up and announce departure
            handleDisconnection();
        }
    }

    /**
     * Sends a message to this client
     * 
     * @param message The message to send
     */
    public void sendMessage(String message) {
        if (out != null && running) {
            try {
                out.println(message);
            } catch (Exception e) {
                System.err.println("Error sending message to " + username + ": " +
                        e.getMessage());
            }
        }
    }

    /**
     * Handles client disconnection
     * Announces departure and removes from server
     */
    private void handleDisconnection() {
        running = false;

        // Announce user left
        if (username != null) {
            String leaveMessage = Message.createSystemMessage(
                    username + " has left the chat.");
            server.broadcastSystemMessage(leaveMessage);

            System.out.println(ConsoleColors.warningMessage("User '" + username + "' left the chat"));
        }

        // Remove from server's client list
        server.removeClient(this);

        // Close connection
        closeConnection();
    }

    /**
     * Closes the client connection and streams
     */
    public void closeConnection() {
        running = false;

        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection for " + username + ": " +
                    e.getMessage());
        }
    }

    /**
     * Gets the username of this client
     * 
     * @return The client's username
     */
    public String getUsername() {
        return username;
    }
}
