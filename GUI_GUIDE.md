# GUI Mode Guide

## 🖥️ Graphical User Interface

The chat application now includes a modern GUI mode alongside the terminal interface!

---

## 🚀 Quick Start

### Launch the Application
```powershell
cd "d:\Java Mini Project"
java -cp bin launcher.ApplicationLauncher
```

This opens the **Application Launcher** where you can choose:
1. **Interface Mode**: GUI or Terminal
2. **Role**: Server or Client

---

## 📱 Application Launcher

The launcher provides an intuitive selection interface:

### Features
- **GUI Mode Button** 🖥️ - Modern graphical interface
- **Terminal Mode Button** ⌨️ - Colorful terminal interface
- **Server Button** 🖧 - Run as chat server
- **Client Button** 💬 - Run as chat client

### How to Use
1. Click your preferred interface mode (GUI or Terminal)
2. Click your role (Server or Client)
3. The application launches automatically!

---

## 🖧 GUI Server Mode

### Window Layout
```
┌─────────────────────────────────────────────────┐
│ File  Help                                      │
├─────────────────────────────────────────────────┤
│ Port: [5000]  [Start Server]                    │
├─────────────────────────────────────────────────┤
│ Connected Clients  │  Message Log               │
│ ┌────────────────┐ │  ┌──────────────────────┐ │
│ │ Alice          │ │  │ ✓ Server started     │ │
│ │ Bob            │ │  │ ✓ Alice joined       │ │
│ │                │ │  │ ✓ Bob joined         │ │
│ │                │ │  │ [14:30] Alice: Hi!   │ │
│ └────────────────┘ │  └──────────────────────┘ │
├─────────────────────────────────────────────────┤
│ Status: Server running on port 5000             │
└─────────────────────────────────────────────────┘
```

### Features
- ✅ **Port Configuration** - Set custom port (default: 5000)
- ✅ **Start/Stop Control** - Easy server management
- ✅ **Client List** - See all connected users
- ✅ **Message Log** - Monitor all chat activity
- ✅ **Status Bar** - Real-time server status
- ✅ **Auto-scroll** - Latest messages always visible

### How to Use
1. **Set Port** (optional) - Default is 5000
2. **Click "Start Server"** - Server begins listening
3. **Monitor Clients** - See users join/leave in real-time
4. **View Messages** - All chat activity logged
5. **Click "Stop Server"** - Gracefully shutdown

---

## 💬 GUI Client Mode

### Window Layout
```
┌─────────────────────────────────────────────────┐
│ File  Help                                      │
├─────────────────────────────────────────────────┤
│ Server: [localhost] Port: [5000]                │
│ Username: [Alice]  [Connect]                    │
├─────────────────────────────────────────────────┤
│ Online Users │  Chat Messages                   │
│ ┌──────────┐ │  ┌────────────────────────────┐ │
│ │ Alice    │ │  │ [14:30] Alice: Hello!      │ │
│ │ Bob      │ │  │ [14:31] Bob: Hi Alice!     │ │
│ │          │ │  │ [14:32] Alice: How are you?│ │
│ └──────────┘ │  └────────────────────────────┘ │
├─────────────────────────────────────────────────┤
│ Message: [Type here...]           [Send]        │
├─────────────────────────────────────────────────┤
│ Status: Connected to localhost:5000             │
└─────────────────────────────────────────────────┘
```

### Features
- ✅ **Connection Panel** - Easy server connection
- ✅ **Colored Messages** - Each user has unique color
- ✅ **Online Users List** - See who's chatting
- ✅ **Message Input** - Type and send messages
- ✅ **Send Button** - Click or press Enter
- ✅ **Status Bar** - Connection status
- ✅ **Auto-scroll** - Latest messages visible

### How to Use
1. **Enter Server IP** - localhost or LAN IP
2. **Enter Port** - Default 5000
3. **Enter Username** - Your display name
4. **Click "Connect"** - Join the chat
5. **Type Message** - In the input field
6. **Press Enter or Click Send** - Send message
7. **Click "Disconnect"** - Leave chat

---

## 🎨 Color Scheme

### GUI uses the same vibrant colors as terminal:
- **User Messages** - 10 unique colors per user
- **System Messages** - Blue
- **Success Messages** - Green (✓)
- **Warnings** - Orange (⚠)
- **Timestamps** - Gray

---

## ⚙️ Menu Options

### File Menu
- **Disconnect** (Client only) - Leave the chat
- **Exit** - Close application

### Help Menu
- **About** - Application information

---

## 🔄 Mode Comparison

| Feature | Terminal Mode | GUI Mode |
|---------|--------------|----------|
| **Interface** | Command-line | Graphical windows |
| **Colors** | ANSI codes | Swing colors |
| **Input** | Keyboard only | Mouse + Keyboard |
| **User List** | Not visible | Always visible |
| **Scrolling** | Terminal scroll | Built-in scrollbar |
| **Best For** | Power users | General users |

---

## 💡 Tips & Tricks

### Server Tips
- Start server before clients connect
- Use port 5000 for simplicity
- Monitor message log for activity
- Stop server gracefully before closing

### Client Tips
- Use `localhost` for same machine
- Use actual IP for LAN connections
- Press Enter to send messages quickly
- Check status bar for connection state

---

## 🌐 LAN Usage

### Server Machine
1. Launch GUI Server
2. Note your IP address (use `ipconfig`)
3. Start the server
4. Share IP with others

### Client Machines
1. Launch GUI Client
2. Enter server's IP address
3. Enter port 5000
4. Choose username and connect

---

## 🔧 Keyboard Shortcuts

### Client
- **Enter** - Send message
- **Alt+F4** - Exit application

### Server
- **Alt+F4** - Exit (prompts if running)

---

## ❓ Troubleshooting

### "Port already in use"
- Change port number
- Close other applications using that port

### "Cannot connect"
- Verify server is running
- Check IP address is correct
- Ensure same network (LAN)
- Check firewall settings

### "Connection lost"
- Server may have stopped
- Network issue
- Restart and reconnect

---

## 🎯 Advantages of GUI Mode

1. **User-Friendly** - Point and click interface
2. **Visual Feedback** - See everything at a glance
3. **Easy Navigation** - Menus and buttons
4. **Professional Look** - Modern appearance
5. **Better for Demos** - Visual impact

---

## 🚀 Running Both Modes

You can run terminal and GUI modes simultaneously:

**Example:**
- GUI Server on one machine
- Terminal clients on others
- Or vice versa!

Both modes are fully compatible and can interoperate seamlessly.

---

## 📸 Screenshots

*Note: The GUI features modern Swing components with:*
- Clean, professional design
- Intuitive layouts
- Proper spacing and borders
- Color-coded messages
- Responsive controls

---

## 🎓 For Academic Presentation

### GUI Demonstrates:
- **Swing/AWT Knowledge** - Modern GUI development
- **Event-Driven Programming** - Button clicks, key presses
- **MVC Pattern** - Separation of UI and logic
- **Thread Safety** - SwingUtilities for GUI updates
- **User Experience Design** - Intuitive interface

### Perfect for Viva:
- Show both terminal and GUI modes
- Explain mode selection
- Demonstrate live chat with GUI
- Highlight OOP principles in GUI code

---

**Enjoy the modern GUI experience! 🎉**
