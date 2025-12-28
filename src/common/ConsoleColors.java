package common;

/**
 * ConsoleColors - ANSI color codes for terminal output
 * 
 * This utility class provides ANSI escape codes for colorful terminal output.
 * Makes the chat interface more visually appealing and user-friendly.
 * 
 * Features:
 * - Different colors for different users
 * - Distinct styling for system messages
 * - Timestamp highlighting
 * - Error message formatting
 */
public class ConsoleColors {

    // Reset code - returns to default terminal color
    public static final String RESET = "\u001B[0m";

    // Text colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright/Bold colors
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // Background colors
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // Text styles
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String REVERSED = "\u001B[7m";

    // User colors - array of vibrant colors for different users
    private static final String[] USER_COLORS = {
            BRIGHT_CYAN, // User 1
            BRIGHT_GREEN, // User 2
            BRIGHT_YELLOW, // User 3
            BRIGHT_PURPLE, // User 4
            BRIGHT_BLUE, // User 5
            BRIGHT_RED, // User 6
            CYAN, // User 7
            GREEN, // User 8
            YELLOW, // User 9
            PURPLE // User 10
    };

    /**
     * Gets a unique color for a username based on hash
     * Same username always gets the same color
     * 
     * @param username The username to get color for
     * @return ANSI color code
     */
    public static String getUserColor(String username) {
        if (username == null || username.isEmpty()) {
            return WHITE;
        }

        // Use hashCode to consistently assign same color to same user
        int hash = Math.abs(username.hashCode());
        int colorIndex = hash % USER_COLORS.length;
        return USER_COLORS[colorIndex];
    }

    /**
     * Formats a username with its assigned color
     * 
     * @param username The username to format
     * @return Colored username string
     */
    public static String coloredUsername(String username) {
        return getUserColor(username) + BOLD + username + RESET;
    }

    /**
     * Formats a timestamp with subtle gray color
     * 
     * @param timestamp The timestamp string
     * @return Colored timestamp
     */
    public static String coloredTimestamp(String timestamp) {
        return BRIGHT_BLACK + timestamp + RESET;
    }

    /**
     * Formats a system message with distinct styling
     * 
     * @param message The system message
     * @return Formatted system message
     */
    public static String systemMessage(String message) {
        return BRIGHT_YELLOW + BOLD + "⚡ " + message + RESET;
    }

    /**
     * Formats an error message with red color
     * 
     * @param message The error message
     * @return Formatted error message
     */
    public static String errorMessage(String message) {
        return BRIGHT_RED + BOLD + "❌ " + message + RESET;
    }

    /**
     * Formats a success message with green color
     * 
     * @param message The success message
     * @return Formatted success message
     */
    public static String successMessage(String message) {
        return BRIGHT_GREEN + BOLD + "✓ " + message + RESET;
    }

    /**
     * Formats an info message with blue color
     * 
     * @param message The info message
     * @return Formatted info message
     */
    public static String infoMessage(String message) {
        return BRIGHT_BLUE + "ℹ " + message + RESET;
    }

    /**
     * Formats a warning message with yellow color
     * 
     * @param message The warning message
     * @return Formatted warning message
     */
    public static String warningMessage(String message) {
        return YELLOW + BOLD + "⚠ " + message + RESET;
    }

    /**
     * Creates a colored header/banner
     * 
     * @param text The header text
     * @return Formatted header
     */
    public static String header(String text) {
        return BRIGHT_CYAN + BOLD + text + RESET;
    }

    /**
     * Creates a colored separator line
     * 
     * @param length Length of the separator
     * @return Colored separator
     */
    public static String separator(int length) {
        return BRIGHT_BLACK + "─".repeat(length) + RESET;
    }

    /**
     * Wraps text in a specific color
     * 
     * @param text  The text to color
     * @param color The ANSI color code
     * @return Colored text
     */
    public static String colored(String text, String color) {
        return color + text + RESET;
    }
}
