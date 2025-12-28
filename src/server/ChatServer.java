package server;

import common.MessageBroadcaster;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatServer - Main server class implementing MessageBroadcaster interface
 * 
 * This class manages the chat server, accepts client connections,
 * and coordinates message broadcasting.
 * 
 * OOP Principles Demonstrated:
 * - Interface Implementation: Implements MessageBroadcaster
 * - Composition: Contains a list of ClientHandler objects
 * - Encapsulation: Private fields and methods
 * - Single Responsibility: Manages server operations only
 * 
 * Multithreading:
 * - Main thread accepts connections
 * - Each client runs on separate ClientHandler thread
 * - Synchronized access to shared client list
 */
public class ChatServer implements MessageBroadcaster {
    
    // Server configuration
    private static final int DEFAULT_PORT = 5000;
    private final int port;
    
    // Server socket for accepting connections
    private ServerSocket serverSocket;
    
    // List of connected clients - shared resource, needs synchronization
    private final List<ClientHandler> clients;
    
    // Server running state
    private volatile boolean running;
    
    /**
     * Constructor - Creates server with default port
     */
    public ChatServer() {
        this(DEFAULT_PORT);
    }
    
    /**
     * Constructor - Creates server with specified port
     * 
     * @param port The port number to listen on
     */
    public ChatServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.running = false;
    }
    
    /**
     * Starts the chat server
     * 
     * This method:
     * 1. Creates a ServerSocket
     * 2. Listens for client connections
     * 3. Creates a ClientHandler thread for each client
     * 4. Handles all server-side errors gracefully
     */
    public void start() {
        try {
            // Create server socket
            serverSocket = new ServerSocket(port);
            running = true;
            
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║     CHAT SERVER STARTED SUCCESSFULLY       ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("Server is listening on port: " + port);
            System.out.println("Waiting for clients to connect...\n");
            
            // Main server loop - accepts client connections
            while (running) {
                try {
                    // Accept client connection (blocking call)
                    Socket clientSocket = serverSocket.accept();
                    
                    // Create new ClientHandler thread for this client
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    
                    // Add to client list (synchronized for thread safety)
                    synchronized (clients) {
                        clients.add(clientHandler);
                    }
                    
                    // Start the client handler thread
                    clientHandler.start();
                    
                    System.out.println("New client connected from: " + 
                        clientSocket.getInetAddress().getHostAddress());
                    System.out.println("Total clients: " + clients.size() + "\n");
                    
                } catch (SocketException e) {
                    // Server socket closed - normal shutdown
                    if (!running) {
                        System.out.println("Server is shutting down...");
                    }
                } catch (IOException e) {
                    // Error accepting client connection
                    if (running) {
                        System.err.println("Error accepting client connection: " + 
                            e.getMessage());
                    }
                }
            }
            
        } catch (BindException e) {
            // Port already in use - critical error
            System.err.println("╔════════════════════════════════════════════╗");
            System.err.println("║              ERROR: PORT IN USE            ║");
            System.err.println("╚════════════════════════════════════════════╝");
            System.err.println("Port " + port + " is already in use.");
            System.err.println("Please try one of the following:");
            System.err.println("1. Close the application using port " + port);
            System.err.println("2. Use a different port number");
            System.err.println("3. Wait a few moments and try again\n");
            
        } catch (IOException e) {
            // Other I/O errors during server startup
            System.err.println("╔════════════════════════════════════════════╗");
            System.err.println("║          ERROR: SERVER STARTUP FAILED      ║");
            System.err.println("╚════════════════════════════════════════════╝");
            System.err.println("Could not start server: " + e.getMessage());
            System.err.println("Please check your network configuration.\n");
            
        } finally {
            // Cleanup resources
            shutdown();
        }
    }
    
    /**
     * Broadcasts a message to all clients except the sender
     * Synchronized to ensure thread-safe access to client list
     * 
     * @param message The message to broadcast
     * @param sender The ClientHandler who sent the message
     */
    @Override
    public void broadcastMessage(String message, Object sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                // Don't send message back to sender
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
    
    /**
     * Broadcasts a system message to all clients
     * Synchronized to ensure thread-safe access to client list
     * 
     * @param message The system message to broadcast
     */
    @Override
    public void broadcastSystemMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }
    
    /**
     * Gets the number of currently connected clients
     * 
     * @return Number of active clients
     */
    @Override
    public int getActiveClientCount() {
        synchronized (clients) {
            return clients.size();
        }
    }
    
    /**
     * Removes a client from the active client list
     * Called when a client disconnects
     * 
     * @param clientHandler The client to remove
     */
    public void removeClient(ClientHandler clientHandler) {
        synchronized (clients) {
            clients.remove(clientHandler);
            System.out.println("Client disconnected. Total clients: " + 
                clients.size() + "\n");
        }
    }
    
    /**
     * Shuts down the server gracefully
     * Closes all client connections and server socket
     */
    public void shutdown() {
        running = false;
        
        try {
            // Close all client connections
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.closeConnection();
                }
                clients.clear();
            }
            
            // Close server socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            
            System.out.println("\nServer shut down successfully.");
            
        } catch (IOException e) {
            System.err.println("Error during server shutdown: " + e.getMessage());
        }
    }
    
    /**
     * Main method - Entry point for server application
     * 
     * @param args Command line arguments (optional: port number)
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        // Check if custom port is provided
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                
                // Validate port range
                if (port < 1024 || port > 65535) {
                    System.err.println("Invalid port number. Using default port " + 
                        DEFAULT_PORT);
                    System.err.println("Valid port range: 1024-65535\n");
                    port = DEFAULT_PORT;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid port format. Using default port " + 
                    DEFAULT_PORT + "\n");
            }
        }
        
        // Create and start server
        ChatServer server = new ChatServer(port);
        
        // Add shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutdown signal received...");
            server.shutdown();
        }));
        
        // Start server
        server.start();
    }
}
