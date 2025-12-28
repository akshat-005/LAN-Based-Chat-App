package common;

/**
 * MessageBroadcaster Interface - Demonstrates ABSTRACTION and INTERFACE-BASED DESIGN
 * 
 * This interface defines the contract for any class that needs to
 * broadcast messages to multiple recipients.
 * 
 * OOP Principles Demonstrated:
 * - Abstraction: Defines what to do, not how to do it
 * - Interface Segregation: Single, focused responsibility
 * - Polymorphism: Different implementations can exist
 * 
 * Benefits:
 * - Loose coupling between components
 * - Easy to test and mock
 * - Allows multiple broadcasting strategies
 */
public interface MessageBroadcaster {
    
    /**
     * Broadcasts a message to all connected clients
     * 
     * @param message The message to broadcast
     * @param sender The client who sent the message (to exclude from broadcast)
     */
    void broadcastMessage(String message, Object sender);
    
    /**
     * Broadcasts a system message to all clients
     * 
     * @param message The system message to broadcast
     */
    void broadcastSystemMessage(String message);
    
    /**
     * Gets the count of currently connected clients
     * 
     * @return Number of active connections
     */
    int getActiveClientCount();
}
