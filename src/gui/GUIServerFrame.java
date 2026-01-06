package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import server.ChatServer;
import common.ConsoleColors;

/**
 * GUIServerFrame - Graphical interface for the chat server
 * 
 * Provides a modern Swing-based UI for server operations:
 * - Start/Stop server
 * - Configure port
 * - View connected clients
 * - Monitor message log
 */
public class GUIServerFrame extends JFrame {

    // UI Components
    private JTextField portField;
    private JButton startStopButton;
    private DefaultListModel<String> clientListModel;
    private JList<String> clientList;
    private JTextArea messageLogArea;
    private JLabel statusLabel;
    private JLabel clientCountLabel;

    // Server instance
    private ChatServer server;
    private Thread serverThread;
    private boolean serverRunning = false;

    /**
     * Constructor - Creates and displays the server GUI
     */
    public GUIServerFrame() {
        super("Chat Server - GUI Mode");
        initializeComponents();
        setupLayout();
        setupListeners();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes all UI components
     */
    private void initializeComponents() {
        // Port configuration
        portField = new JTextField("5000", 10);
        portField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Start/Stop button
        startStopButton = new JButton("Start Server");
        startStopButton.setFont(new Font("Arial", Font.BOLD, 14));
        startStopButton.setBackground(new Color(46, 204, 113));
        startStopButton.setForeground(Color.WHITE);
        startStopButton.setFocusPainted(false);
        startStopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Client list
        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        clientList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Message log
        messageLogArea = new JTextArea();
        messageLogArea.setEditable(false);
        messageLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        messageLogArea.setLineWrap(true);
        messageLogArea.setWrapStyleWord(true);

        // Status label
        statusLabel = new JLabel("Server stopped");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.GRAY);

        // Client count label
        clientCountLabel = new JLabel("Connected Clients: 0");
        clientCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }

    /**
     * Sets up the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Top panel - Controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(new TitledBorder("Server Controls"));
        controlPanel.add(new JLabel("Port:"));
        controlPanel.add(portField);
        controlPanel.add(startStopButton);

        add(controlPanel, BorderLayout.NORTH);

        // Center panel - Split between client list and message log
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        // Left - Client list
        JPanel clientPanel = new JPanel(new BorderLayout(5, 5));
        clientPanel.setBorder(new TitledBorder("Connected Clients"));
        clientPanel.add(clientCountLabel, BorderLayout.NORTH);
        clientPanel.add(new JScrollPane(clientList), BorderLayout.CENTER);

        // Right - Message log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(new TitledBorder("Message Log"));
        logPanel.add(new JScrollPane(messageLogArea), BorderLayout.CENTER);

        splitPane.setLeftComponent(clientPanel);
        splitPane.setRightComponent(logPanel);

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
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            if (serverRunning) {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Server is running. Are you sure you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    stopServer();
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        });
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
        startStopButton.addActionListener(e -> {
            if (serverRunning) {
                stopServer();
            } else {
                startServer();
            }
        });

        // Window closing listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (serverRunning) {
                    stopServer();
                }
            }
        });
    }

    /**
     * Starts the chat server
     */
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText().trim());

            if (port < 1024 || port > 65535) {
                JOptionPane.showMessageDialog(
                        this,
                        "Port must be between 1024 and 65535",
                        "Invalid Port",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and start server in separate thread
            server = new ChatServer(port);
            serverThread = new Thread(() -> server.start());
            serverThread.start();

            serverRunning = true;
            portField.setEnabled(false);
            startStopButton.setText("Stop Server");
            startStopButton.setBackground(new Color(231, 76, 60));
            statusLabel.setText("Server running on port " + port);
            statusLabel.setForeground(new Color(46, 204, 113));

            appendLog("✓ Server started successfully on port " + port);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid port number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Stops the chat server
     */
    private void stopServer() {
        if (server != null) {
            server.shutdown();
            serverRunning = false;
            portField.setEnabled(true);
            startStopButton.setText("Start Server");
            startStopButton.setBackground(new Color(46, 204, 113));
            statusLabel.setText("Server stopped");
            statusLabel.setForeground(Color.GRAY);

            appendLog("⚠ Server stopped");

            // Clear client list
            clientListModel.clear();
            updateClientCount();
        }
    }

    /**
     * Appends a message to the log
     */
    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            messageLogArea.append(message + "\n");
            messageLogArea.setCaretPosition(messageLogArea.getDocument().getLength());
        });
    }

    /**
     * Adds a client to the list
     */
    public void addClient(String clientName) {
        SwingUtilities.invokeLater(() -> {
            clientListModel.addElement(clientName);
            updateClientCount();
            appendLog("✓ " + clientName + " joined the chat");
        });
    }

    /**
     * Removes a client from the list
     */
    public void removeClient(String clientName) {
        SwingUtilities.invokeLater(() -> {
            clientListModel.removeElement(clientName);
            updateClientCount();
            appendLog("⚠ " + clientName + " left the chat");
        });
    }

    /**
     * Updates the client count label
     */
    private void updateClientCount() {
        int count = clientListModel.getSize();
        clientCountLabel.setText("Connected Clients: " + count);
    }

    /**
     * Shows the about dialog
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "LAN Chat Application - Server\n" +
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

        SwingUtilities.invokeLater(() -> new GUIServerFrame());
    }
}
