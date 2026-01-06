package launcher;

import javax.swing.*;
import java.awt.*;
import server.ChatServer;
import client.ChatClient;
import gui.GUIServerFrame;
import gui.GUIClientFrame;

/**
 * ApplicationLauncher - Main entry point with mode selection
 * 
 * Allows users to choose between:
 * 1. Terminal Mode (existing colorful CLI)
 * 2. GUI Mode (modern Swing interface)
 * 
 * And select their role:
 * - Server
 * - Client
 */
public class ApplicationLauncher {

    private static final String APP_TITLE = "LAN Chat Application";
    private static final String VERSION = "v2.0";

    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system one fails
        }

        // Show launcher dialog
        SwingUtilities.invokeLater(() -> showLauncher());
    }

    /**
     * Shows the launcher dialog for mode and role selection
     */
    private static void showLauncher() {
        JDialog launcher = new JDialog();
        launcher.setTitle(APP_TITLE + " " + VERSION);
        launcher.setModal(true);
        launcher.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        launcher.setLayout(new BorderLayout(10, 10));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(APP_TITLE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel versionLabel = new JLabel(VERSION);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        versionLabel.setForeground(new Color(236, 240, 241));

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(versionLabel);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mode selection
        JLabel modeLabel = new JLabel("Select Interface Mode:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(modeLabel, gbc);

        JButton guiModeButton = createStyledButton("🖥️ GUI Mode", new Color(46, 204, 113));
        JButton terminalModeButton = createStyledButton("⌨️ Terminal Mode", new Color(52, 152, 219));

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(guiModeButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(terminalModeButton, gbc);

        // Role selection
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(roleLabel, gbc);

        JButton serverButton = createStyledButton("🖧 Server", new Color(155, 89, 182));
        JButton clientButton = createStyledButton("💬 Client", new Color(230, 126, 34));

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        mainPanel.add(serverButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(clientButton, gbc);

        // Info label
        JLabel infoLabel = new JLabel(
                "<html><center>Choose your preferred interface mode<br>and your role to get started</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(infoLabel, gbc);

        // Track selections
        final String[] selectedMode = { null };
        final String[] selectedRole = { null };

        // Mode button listeners
        guiModeButton.addActionListener(e -> {
            selectedMode[0] = "GUI";
            guiModeButton.setBackground(new Color(39, 174, 96));
            terminalModeButton.setBackground(new Color(52, 152, 219));
            checkAndLaunch(launcher, selectedMode[0], selectedRole[0]);
        });

        terminalModeButton.addActionListener(e -> {
            selectedMode[0] = "TERMINAL";
            terminalModeButton.setBackground(new Color(41, 128, 185));
            guiModeButton.setBackground(new Color(46, 204, 113));
            checkAndLaunch(launcher, selectedMode[0], selectedRole[0]);
        });

        // Role button listeners
        serverButton.addActionListener(e -> {
            selectedRole[0] = "SERVER";
            serverButton.setBackground(new Color(142, 68, 173));
            clientButton.setBackground(new Color(230, 126, 34));
            checkAndLaunch(launcher, selectedMode[0], selectedRole[0]);
        });

        clientButton.addActionListener(e -> {
            selectedRole[0] = "CLIENT";
            clientButton.setBackground(new Color(211, 84, 0));
            serverButton.setBackground(new Color(155, 89, 182));
            checkAndLaunch(launcher, selectedMode[0], selectedRole[0]);
        });

        // Add panels to dialog
        launcher.add(headerPanel, BorderLayout.NORTH);
        launcher.add(mainPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel footerLabel = new JLabel("© 2024 LAN Chat Application - OOP Project");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        launcher.add(footerPanel, BorderLayout.SOUTH);

        launcher.pack();
        launcher.setLocationRelativeTo(null);
        launcher.setVisible(true);
    }

    /**
     * Creates a styled button with hover effects
     */
    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    /**
     * Checks if both selections are made and launches the application
     */
    private static void checkAndLaunch(JDialog launcher, String mode, String role) {
        if (mode != null && role != null) {
            launcher.dispose();
            launchApplication(mode, role);
        }
    }

    /**
     * Launches the appropriate application based on selections
     */
    private static void launchApplication(String mode, String role) {
        if (mode.equals("GUI")) {
            // Launch GUI mode
            if (role.equals("SERVER")) {
                SwingUtilities.invokeLater(() -> new GUIServerFrame());
            } else {
                SwingUtilities.invokeLater(() -> new GUIClientFrame());
            }
        } else {
            // Launch Terminal mode
            if (role.equals("SERVER")) {
                new Thread(() -> ChatServer.main(new String[] {})).start();
            } else {
                new Thread(() -> ChatClient.main(new String[] {})).start();
            }
        }
    }
}
