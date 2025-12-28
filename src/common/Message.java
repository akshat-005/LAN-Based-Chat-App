package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Message class - Demonstrates ENCAPSULATION and DATA ABSTRACTION
 * 
 * This class encapsulates all message-related data and provides
 * a clean interface for creating and formatting messages.
 * 
 * OOP Principles Demonstrated:
 * - Encapsulation: Private fields with public getters
 * - Data Abstraction: Hides internal representation
 * - Validation: Ensures data integrity
 */
public class Message {
    // Private fields - Encapsulation
    private final String sender;
    private final String content;
    private final LocalDateTime timestamp;

    // Date formatter for consistent timestamp display
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Constructor - Creates a new message with current timestamp
     * 
     * @param sender  The username of the message sender
     * @param content The actual message content
     * @throws IllegalArgumentException if sender or content is null/empty
     */
    public Message(String sender, String content) {
        // Validation - ensures data integrity
        if (sender == null || sender.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        this.sender = sender.trim();
        this.content = content.trim();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets the sender's username
     * 
     * @return The sender's username
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the message content
     * 
     * @return The message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the message timestamp
     * 
     * @return The timestamp when message was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Formats the message for display with colors
     * Format: [HH:mm:ss] Sender: Content
     * Each user gets a unique color based on their username
     * 
     * @return Formatted message string with ANSI colors
     */
    public String getFormattedMessage() {
        String coloredTime = ConsoleColors.coloredTimestamp(
                "[" + timestamp.format(TIME_FORMATTER) + "]");
        String coloredSender = ConsoleColors.coloredUsername(sender);

        return String.format("%s %s: %s",
                coloredTime,
                coloredSender,
                content);
    }

    /**
     * Creates a system message (no specific sender)
     * Used for server announcements
     * 
     * @param content The system message content
     * @return A formatted system message with distinct styling
     */
    public static String createSystemMessage(String content) {
        LocalDateTime now = LocalDateTime.now();
        String coloredTime = ConsoleColors.coloredTimestamp(
                "[" + now.format(TIME_FORMATTER) + "]");
        String serverLabel = ConsoleColors.colored("SERVER", ConsoleColors.BRIGHT_YELLOW + ConsoleColors.BOLD);

        return String.format("%s %s: %s",
                coloredTime,
                serverLabel,
                ConsoleColors.systemMessage(content));
    }

    /**
     * Validates if a message string is not empty
     * 
     * @param message The message to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}
