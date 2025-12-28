package client;

import common.Message;
import common.ConsoleColors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * ChatClient - Main client class for connecting to chat server
 * 
 * This class handles:
 * - Connecting to the server
 * - Sending messages to server
 * - Creating MessageReceiver thread for receiving messages
 * - Handling user input
 * - Graceful disconnection
 * 
 * OOP Principles Demonstrated:
 * - Composition: Contains MessageReceiver thread
 * - Encapsulation: Private fields and methods
 * - Single Responsibility: Manages client operations only
 * 
 * Multithreading:
 * - Main thread handles user input and sending
 * - MessageReceiver thread handles receiving messages
 */
public class ChatClient {

    // Server connection details
    private String serverAddress;
    private int serverPort;

    // Network components
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Message receiver thread
    private MessageReceiver messageReceiver;

    // User information
    private String username;

    // Connection state
    private volatile boolean connected;

    /**
     * Constructor - Creates a new chat client
     */
    public ChatClient() {
        this.connected = false;
    }

    /**
     * Connects to the chat server
     * 
     * @param serverAddress Server IP address or hostname
     * @param serverPort    Server port number
     * @param username      User's chosen username
     * @return true if connection successful, false otherwise
     */
    public boolean connect(String serverAddress, int serverPort, String username) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.username = username;

        try {
            // Validate inputs
            if (username == null || username.trim().isEmpty()) {
                System.err.println("╔════════════════════════════════════════════╗");
                System.err.println("║          ERROR: INVALID USERNAME           ║");
                System.err.println("╚════════════════════════════════════════════╝");
                System.err.println("Username cannot be empty.\n");
                return false;
            }

            System.out.println("Connecting to server at " + serverAddress + ":" +
                    serverPort + "...");

            // Create socket connection
            socket = new Socket(serverAddress, serverPort);

            // Set up I/O streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Wait for username prompt
            String prompt = in.readLine();
            if (prompt != null && prompt.equals("ENTER_USERNAME")) {
                // Send username
                out.println(username);

                // Wait for confirmation
                String response = in.readLine();
                if (response != null && response.startsWith("SUCCESS")) {
                    connected = true;

                    System.out.println(ConsoleColors.BRIGHT_CYAN + "╔════════════════════════════════════════════╗"
                            + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.BRIGHT_CYAN + "║       CONNECTED TO CHAT SERVER             ║"
                            + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.BRIGHT_CYAN + "╚════════════════════════════════════════════╝"
                            + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.successMessage(response.substring(9))); // Remove "SUCCESS: "
                    System.out.println("\n" + ConsoleColors.infoMessage("You can start chatting now!"));
                    System.out.println(ConsoleColors.warningMessage("Type 'exit' to disconnect.") + "\n");

                    // Start message receiver thread
                    messageReceiver = new MessageReceiver(in, this);
                    messageReceiver.start();

                    return true;

                } else if (response != null && response.startsWith("ERROR")) {
                    System.err.println(ConsoleColors.errorMessage("Server rejected connection: " +
                            response.substring(7)));
                    disconnect();
                    return false;
                }
            }

            System.err.println(ConsoleColors.errorMessage("Unexpected server response. Connection failed."));
            disconnect();
            return false;

        } catch (UnknownHostException e) {
            // Invalid server address
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╔════════════════════════════════════════════╗" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "║        ERROR: INVALID SERVER ADDRESS       ║" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╚════════════════════════════════════════════╝" + ConsoleColors.RESET);
            System.err.println(ConsoleColors.errorMessage("Could not find server: " + serverAddress));
            System.err.println(ConsoleColors.warningMessage("Please check the IP address and try again.") + "\n");
            return false;

        } catch (ConnectException e) {
            // Server not reachable or connection refused
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╔════════════════════════════════════════════╗" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "║       ERROR: CONNECTION REFUSED            ║" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╚════════════════════════════════════════════╝" + ConsoleColors.RESET);
            System.err.println(ConsoleColors.errorMessage("Could not connect to server at " + serverAddress +
                    ":" + serverPort));
            System.err.println("\n" + ConsoleColors.warningMessage("Possible reasons:"));
            System.err.println("  1. Server is not running");
            System.err.println("  2. Wrong IP address or port number");
            System.err.println("  3. Firewall blocking the connection");
            System.err.println("  4. Not on the same network\n");
            return false;

        } catch (IOException e) {
            // Other I/O errors
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╔════════════════════════════════════════════╗" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "║         ERROR: CONNECTION FAILED           ║" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╚════════════════════════════════════════════╝" + ConsoleColors.RESET);
            System.err.println(ConsoleColors.errorMessage("I/O error: " + e.getMessage()));
            System.err.println(ConsoleColors.warningMessage("Please check your network connection.") + "\n");
            return false;

        } catch (Exception e) {
            // Unexpected errors
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╔════════════════════════════════════════════╗" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "║         ERROR: UNEXPECTED ERROR            ║" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╚════════════════════════════════════════════╝" + ConsoleColors.RESET);
            System.err.println(ConsoleColors.errorMessage("An unexpected error occurred: " + e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a message to the server
     * 
     * @param message The message to send
     */
    public void sendMessage(String message) {
        if (!connected) {
            System.err.println(ConsoleColors.errorMessage("Not connected to server"));
            return;
        }

        // Validate message
        if (message == null || message.trim().isEmpty()) {
            System.err.println(ConsoleColors.errorMessage("Cannot send empty message"));
            return;
        }

        try {
            // Send message to server
            out.println(message);

        } catch (Exception e) {
            System.err.println("ERROR: Failed to send message: " + e.getMessage());
            handleServerDisconnection();
        }
    }

    /**
     * Handles server disconnection
     * Called when connection to server is lost
     */
    public void handleServerDisconnection() {
        if (connected) {
            System.err.println("\n" + ConsoleColors.BRIGHT_RED + "╔════════════════════════════════════════════╗"
                    + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "║      CONNECTION TO SERVER LOST             ║" + ConsoleColors.RESET);
            System.err.println(
                    ConsoleColors.BRIGHT_RED + "╚════════════════════════════════════════════╝" + ConsoleColors.RESET);
            System.err.println(ConsoleColors.errorMessage("The server has disconnected."));
            System.err.println(ConsoleColors.warningMessage("Please restart the client to reconnect.") + "\n");

            disconnect();
            System.exit(0);
        }
    }

    /**
     * Disconnects from the server gracefully
     */
    public void disconnect() {
        connected = false;

        try {
            // Stop message receiver thread
            if (messageReceiver != null) {
                messageReceiver.stopReceiving();
            }

            // Close streams and socket
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            System.out.println("\n" + ConsoleColors.successMessage("Disconnected from server."));

        } catch (IOException e) {
            System.err.println("Error during disconnection: " + e.getMessage());
        }
    }

    /**
     * Starts the client and handles user interaction
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        try {
            // Display welcome banner
            System.out.println(
                    ConsoleColors.BRIGHT_CYAN + "╔════════════════════════════════════════════╗" + ConsoleColors.RESET);
            System.out.println(
                    ConsoleColors.BRIGHT_CYAN + "║         LAN CHAT CLIENT v1.0               ║" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.BRIGHT_CYAN + "╚════════════════════════════════════════════╝"
                    + ConsoleColors.RESET + "\n");

            // Get server address
            System.out.print("Enter server IP address (or 'localhost'): ");
            String address = scanner.nextLine().trim();

            if (address.isEmpty()) {
                System.err.println(ConsoleColors.errorMessage("Server address cannot be empty"));
                return;
            }

            // Get server port
            System.out.print("Enter server port (default 5000): ");
            String portStr = scanner.nextLine().trim();
            int port = 5000;

            if (!portStr.isEmpty()) {
                try {
                    port = Integer.parseInt(portStr);

                    if (port < 1024 || port > 65535) {
                        System.err.println("Invalid port. Using default port 5000");
                        port = 5000;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid port format. Using default port 5000");
                }
            }

            // Get username
            System.out.print("Enter your username: ");
            String user = scanner.nextLine().trim();

            if (user.isEmpty()) {
                System.err.println(ConsoleColors.errorMessage("Username cannot be empty"));
                return;
            }

            System.out.println();

            // Connect to server
            if (!connect(address, port, user)) {
                return;
            }

            // Main message loop
            while (connected) {
                String message = scanner.nextLine();

                // Check for exit command
                if (message.equalsIgnoreCase("exit")) {
                    sendMessage("exit");
                    break;
                }

                // Send message
                if (!message.trim().isEmpty()) {
                    sendMessage(message);
                }
            }

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            disconnect();
            scanner.close();
        }
    }

    /**
     * Main method - Entry point for client application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}
