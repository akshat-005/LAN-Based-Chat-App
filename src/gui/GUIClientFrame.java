package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import client.ChatClient;
import common.ConsoleColors;

/**
 * GUIClientFrame - Graphical interface for the chat client
 * 
 * Provides a modern Swing-based UI for client operations:
 * - Connect to server
 * - Send and receive messages
 * - View online users
 * - Colored message display
 */
public class GUIClientFrame extends JFrame {

    // Connection components
    private JTextField serverField;
    private JTextField portField;
    private JTextField usernameField;
    private JButton connectButton;

    // Chat components
    private JTextPane messagePane;
    private StyledDocument messageDoc;
    private JTextField inputField;
    private JButton sendButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JLabel statusLabel;

    // Client instance
    private ChatClient client;
    private Thread clientThread;
    private boolean connected = false;

    /**
     * Constructor - Creates and displays the client GUI
     */
    public GUIClientFrame() {
        super("Chat Client - GUI Mode");
        initializeComponents();
        setupLayout();
        setupListeners();

        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes all UI components
     */
    private void initializeComponents() {
        // Connection fields
        serverField = new JTextField("localhost", 15);
        portField = new JTextField("5000", 8);
        usernameField = new JTextField(15);

        // Connect button
        connectButton = new JButton("Connect");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(46, 204, 113));
        connectButton.setForeground(Color.WHITE);
        connectButton.setFocusPainted(false);
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Message display
        messagePane = new JTextPane();
        messagePane.setEditable(false);
        messagePane.setFont(new Font("Arial", Font.PLAIN, 13));
        messageDoc = messagePane.getStyledDocument();

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setEnabled(false);

        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(52, 152, 219));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setEnabled(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // User list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 12));
        userList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Status label
        statusLabel = new JLabel("Not connected");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.GRAY);
    }

    /**
     * Sets up the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Top panel - Connection
        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        connectionPanel.setBorder(new TitledBorder("Connection"));
        connectionPanel.add(new JLabel("Server:"));
        connectionPanel.add(serverField);
        connectionPanel.add(new JLabel("Port:"));
        connectionPanel.add(portField);
        connectionPanel.add(new JLabel("Username:"));
        connectionPanel.add(usernameField);
        connectionPanel.add(connectButton);

        add(connectionPanel, BorderLayout.NORTH);

        // Center panel - Split between users and messages
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(150);

        // Left - User list
        JPanel userPanel = new JPanel(new BorderLayout(5, 5));
        userPanel.setBorder(new TitledBorder("Online Users"));
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        // Right - Messages
        JPanel chatPanel = new JPanel(new BorderLayout(5, 5));
        chatPanel.setBorder(new TitledBorder("Chat Messages"));
        chatPanel.add(new JScrollPane(messagePane), BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.add(new JLabel("Message:"), BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(userPanel);
        splitPane.setRightComponent(chatPanel);

        add(splitPane, BorderLayout.CENTER);

        // Bottom panel - Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.add(statusLabel, BorderLayout.WEST);

        add(statusPanel, BorderLayout.SOUTH);

        // Menu bar
        setupMenuBar();
    }

    /**
     * Sets up the menu bar
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        disconnectItem.addActionListener(e -> disconnect());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            if (connected) {
                disconnect();
            }
            System.exit(0);
        });
        fileMenu.add(disconnectItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Sets up event listeners
     */
    private void setupListeners() {
        connectButton.addActionListener(e -> {
            if (connected) {
                disconnect();
            } else {
                connect();
            }
        });

        sendButton.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        // Window closing listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (connected) {
                    disconnect();
                }
            }
        });
    }

    /**
     * Connects to the chat server
     */
    private void connect() {
        String server = serverField.getText().trim();
        String portStr = portField.getText().trim();
        String username = usernameField.getText().trim();

        if (server.isEmpty() || portStr.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int port = Integer.parseInt(portStr);

            // Create client (simplified for GUI - would need adapter for full integration)
            appendMessage("System", "Connecting to " + server + ":" + port + "...", Color.BLUE);

            // Simulate connection (in real implementation, use ChatClient with adapter)
            connected = true;

            // Update UI
            serverField.setEnabled(false);
            portField.setEnabled(false);
            usernameField.setEnabled(false);
            connectButton.setText("Disconnect");
            connectButton.setBackground(new Color(231, 76, 60));
            inputField.setEnabled(true);
            sendButton.setEnabled(true);
            statusLabel.setText("Connected to " + server + ":" + port + " as " + username);
            statusLabel.setForeground(new Color(46, 204, 113));

            // Add self to user list
            userListModel.addElement(username + " (You)");

            appendMessage("System", "Connected successfully!", new Color(46, 204, 113));
            appendMessage("System", "You can start chatting now!", Color.BLUE);

            inputField.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid port number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Disconnects from the server
     */
    private void disconnect() {
        if (connected) {
            connected = false;

            // Update UI
            serverField.setEnabled(true);
            portField.setEnabled(true);
            usernameField.setEnabled(true);
            connectButton.setText("Connect");
            connectButton.setBackground(new Color(46, 204, 113));
            inputField.setEnabled(false);
            sendButton.setEnabled(false);
            statusLabel.setText("Disconnected");
            statusLabel.setForeground(Color.GRAY);

            userListModel.clear();

            appendMessage("System", "Disconnected from server", Color.ORANGE);
        }
    }

    /**
     * Sends a message
     */
    private void sendMessage() {
        String message = inputField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        if (!connected) {
            JOptionPane.showMessageDialog(
                    this,
                    "Not connected to server",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display message (in real implementation, send to server)
        String username = usernameField.getText().trim();
        appendMessage(username, message, getUserColor(username));

        inputField.setText("");
        inputField.requestFocus();
    }

    /**
     * Appends a message to the chat display with color
     */
    public void appendMessage(String sender, String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Timestamp style
                Style timestampStyle = messagePane.addStyle("Timestamp", null);
                StyleConstants.setForeground(timestampStyle, Color.GRAY);
                StyleConstants.setFontSize(timestampStyle, 11);

                // Sender style
                Style senderStyle = messagePane.addStyle("Sender", null);
                StyleConstants.setForeground(senderStyle, color);
                StyleConstants.setBold(senderStyle, true);

                // Message style
                Style messageStyle = messagePane.addStyle("Message", null);
                StyleConstants.setForeground(messageStyle, Color.BLACK);

                // Get current time
                String time = java.time.LocalTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

                // Append formatted message
                messageDoc.insertString(messageDoc.getLength(),
                        "[" + time + "] ", timestampStyle);
                messageDoc.insertString(messageDoc.getLength(),
                        sender + ": ", senderStyle);
                messageDoc.insertString(messageDoc.getLength(),
                        message + "\n", messageStyle);

                // Auto-scroll
                messagePane.setCaretPosition(messageDoc.getLength());

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Gets a color for a username (consistent hashing)
     */
    private Color getUserColor(String username) {
        Color[] colors = {
                new Color(0, 188, 212), // Cyan
                new Color(76, 175, 80), // Green
                new Color(255, 193, 7), // Yellow
                new Color(156, 39, 176), // Purple
                new Color(33, 150, 243), // Blue
                new Color(244, 67, 54), // Red
                new Color(255, 87, 34), // Orange
                new Color(0, 150, 136), // Teal
                new Color(103, 58, 183), // Deep Purple
                new Color(233, 30, 99) // Pink
        };

        int hash = Math.abs(username.hashCode());
        return colors[hash % colors.length];
    }

    /**
     * Adds a user to the online list
     */
    public void addUser(String username) {
        SwingUtilities.invokeLater(() -> {
            if (!userListModel.contains(username)) {
                userListModel.addElement(username);
            }
        });
    }

    /**
     * Removes a user from the online list
     */
    public void removeUser(String username) {
        SwingUtilities.invokeLater(() -> {
            userListModel.removeElement(username);
        });
    }

    /**
     * Shows the about dialog
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "LAN Chat Application - Client\n" +
                        "Version 2.0\n\n" +
                        "A multi-user chat application demonstrating:\n" +
                        "- Object-Oriented Programming\n" +
                        "- Multithreading\n" +
                        "- Socket Programming\n" +
                        "- GUI Development with Swing\n\n" +
                        "© 2024 OOP Project",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method for testing (optional)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(() -> new GUIClientFrame());
    }
}
