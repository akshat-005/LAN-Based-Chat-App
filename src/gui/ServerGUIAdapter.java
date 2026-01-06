package gui;

import server.ChatServer;
import server.ClientHandler;
import javax.swing.*;

/**
 * ServerGUIAdapter - Bridges ChatServer with GUIServerFrame
 * 
 * This adapter allows the existing ChatServer to update the GUI
 * in a thread-safe manner.
 */
public class ServerGUIAdapter extends ChatServer {

    private GUIServerFrame guiFrame;

    /**
     * Constructor with GUI frame reference
     */
    public ServerGUIAdapter(int port, GUIServerFrame guiFrame) {
        super(port);
        this.guiFrame = guiFrame;
    }

    /**
     * Notifies GUI when a client connects
     */
    public void notifyClientConnected(String username) {
        if (guiFrame != null) {
            SwingUtilities.invokeLater(() -> {
                guiFrame.addClient(username);
            });
        }
    }

    /**
     * Notifies GUI when a client disconnects
     */
    public void notifyClientDisconnected(String username) {
        if (guiFrame != null) {
            SwingUtilities.invokeLater(() -> {
                guiFrame.removeClient(username);
            });
        }
    }

    /**
     * Logs a message to the GUI
     */
    public void logMessage(String message) {
        if (guiFrame != null) {
            SwingUtilities.invokeLater(() -> {
                guiFrame.appendLog(message);
            });
        }
    }
}
