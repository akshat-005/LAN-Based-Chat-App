package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

/**
 * MessageReceiver - Receives messages from server on separate thread
 * 
 * This class demonstrates INHERITANCE and POLYMORPHISM by extending Thread.
 * It runs independently to receive messages without blocking user input.
 * 
 * OOP Principles Demonstrated:
 * - Inheritance: Extends Thread class
 * - Polymorphism: Overrides run() method
 * - Encapsulation: Private fields
 * - Single Responsibility: Only receives messages
 * 
 * Multithreading:
 * - Runs on separate thread from main client
 * - Allows simultaneous sending and receiving
 * - Non-blocking message reception
 */
public class MessageReceiver extends Thread {
    
    // Input stream for receiving messages
    private final BufferedReader in;
    
    // Reference to parent client
    private final ChatClient client;
    
    // Running state
    private volatile boolean running;
    
    /**
     * Constructor - Creates a message receiver
     * 
     * @param in BufferedReader connected to server
     * @param client Reference to parent ChatClient
     */
    public MessageReceiver(BufferedReader in, ChatClient client) {
        this.in = in;
        this.client = client;
        this.running = true;
        
        // Set as daemon thread so it doesn't prevent JVM shutdown
        setDaemon(true);
    }
    
    /**
     * run() method - Overrides Thread.run() (POLYMORPHISM)
     * 
     * Continuously listens for messages from server and displays them.
     * Handles server disconnection gracefully.
     */
    @Override
    public void run() {
        try {
            String message;
            
            // Main receive loop
            while (running && (message = in.readLine()) != null) {
                // Display received message
                System.out.println(message);
            }
            
            // If we exit the loop, server disconnected
            if (running) {
                client.handleServerDisconnection();
            }
            
        } catch (SocketException e) {
            // Socket closed - normal if client is disconnecting
            if (running) {
                System.err.println("\nConnection to server lost: " + e.getMessage());
                client.handleServerDisconnection();
            }
            
        } catch (IOException e) {
            // I/O error while receiving
            if (running) {
                System.err.println("\nError receiving message: " + e.getMessage());
                client.handleServerDisconnection();
            }
            
        } catch (Exception e) {
            // Unexpected error
            if (running) {
                System.err.println("\nUnexpected error in message receiver: " + 
                    e.getMessage());
                e.printStackTrace();
                client.handleServerDisconnection();
            }
        }
    }
    
    /**
     * Stops the message receiver thread
     */
    public void stopReceiving() {
        running = false;
        
        // Interrupt if blocked on read
        interrupt();
    }
}
