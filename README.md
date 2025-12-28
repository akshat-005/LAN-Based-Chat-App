# LAN-Based Multi-User Terminal Chat Application

A complete, production-ready terminal chat application built in Java.

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Prerequisites](#prerequisites)
- [Compilation Instructions](#compilation-instructions)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Sample Output](#sample-output)
- [Troubleshooting](#troubleshooting)
- [Project Structure](#project-structure)

## 🎯 Project Overview

This is a LAN-based multi-user terminal chat application that allows multiple users on the same local network to communicate in real-time. The system uses a client-server architecture with TCP sockets for reliable communication.

**Key Components:**
- **Chat Server**: Accepts multiple client connections and broadcasts messages
- **Chat Client**: Connects to server and enables real-time messaging
- **Multithreading**: Each client runs on a separate thread
- **Error Handling**: Comprehensive exception handling for all scenarios

## ✨ Features

### Server Features
- ✅ Accepts multiple simultaneous client connections
- ✅ Creates dedicated thread for each client
- ✅ Broadcasts messages to all connected clients
- ✅ Detects and handles client disconnections gracefully
- ✅ Displays detailed server logs
- ✅ Configurable port number
- ✅ Graceful shutdown handling

### Client Features
- ✅ Connects to server using IP address and port
- ✅ User authentication with username
- ✅ Real-time message sending and receiving
- ✅ Asynchronous message reception (separate thread)
- ✅ Formatted message display with timestamps
- ✅ Graceful exit with "exit" command
- ✅ Comprehensive error messages


## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      Chat Server                         │
│  ┌────────────────────────────────────────────────────┐ │
│  │  Main Thread (Accepts Connections)                 │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│       ┌──────────────────┼──────────────────┐           │
│       ▼                  ▼                  ▼           │
│  ┌─────────┐       ┌─────────┐       ┌─────────┐       │
│  │ Client  │       │ Client  │       │ Client  │       │
│  │Handler 1│       │Handler 2│       │Handler N│       │
│  │ Thread  │       │ Thread  │       │ Thread  │       │
│  └─────────┘       └─────────┘       └─────────┘       │
└─────────────────────────────────────────────────────────┘
       │                  │                  │
       │                  │                  │
       ▼                  ▼                  ▼
┌───────────┐      ┌───────────┐      ┌───────────┐
│  Client 1 │      │  Client 2 │      │  Client N │
│           │      │           │      │           │
│ ┌───────┐ │      │ ┌───────┐ │      │ ┌───────┐ │
│ │ Main  │ │      │ │ Main  │ │      │ │ Main  │ │
│ │Thread │ │      │ │Thread │ │      │ │Thread │ │
│ └───────┘ │      │ └───────┘ │      │ └───────┘ │
│ ┌───────┐ │      │ ┌───────┐ │      │ ┌───────┐ │
│ │Message│ │      │ │Message│ │      │ │Message│ │
│ │Receiver│ │      │ │Receiver│ │      │ │Receiver│ │
│ └───────┘ │      │ └───────┘ │      │ └───────┘ │
└───────────┘      └───────────┘      └───────────┘
```

## 📦 Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Network**: All machines must be on the same LAN/Wi-Fi network

## 🔨 Compilation Instructions

### Step 1: Navigate to Project Directory
```powershell
cd "d:\Java Mini Project"
```

### Step 2: Create Output Directory
```powershell
mkdir bin
```

### Step 3: Compile All Java Files
```powershell
javac -d bin src/common/*.java src/server/*.java src/client/*.java
```

**Expected Output:**
- No errors if compilation is successful
- Compiled `.class` files will be in the `bin` directory

### Troubleshooting Compilation
If you encounter errors:
1. Ensure JDK is installed: `java -version`
2. Check JAVA_HOME environment variable
3. Verify all source files are present
4. Check for typos in package names

## 🚀 Running the Application

### Starting the Server

#### On Windows (PowerShell/CMD):
```powershell
cd "d:\Java Mini Project"
java -cp bin server.ChatServer
```

#### On macOS/Linux:
```bash
cd "d:/Java Mini Project"
java -cp bin server.ChatServer
```

#### With Custom Port:
```powershell
java -cp bin server.ChatServer 6000
```

**Expected Output:**
```
╔════════════════════════════════════════════╗
║     CHAT SERVER STARTED SUCCESSFULLY       ║
╚════════════════════════════════════════════╝
Server is listening on port: 5000
Waiting for clients to connect...
```

### Starting the Client

#### Open a New Terminal Window:
```powershell
cd "d:\Java Mini Project"
java -cp bin client.ChatClient
```

**You will be prompted for:**
1. Server IP address (use `localhost` for same machine, or actual IP for LAN)
2. Server port (default: 5000)
3. Your username

### Running on Multiple Machines (LAN)

#### On Server Machine:
1. Start the server as shown above
2. Find your local IP address:
   - **Windows**: `ipconfig` (look for IPv4 Address)
   - **macOS/Linux**: `ifconfig` or `ip addr`
3. Note the IP address (e.g., 192.168.1.100)

#### On Client Machines:
1. Ensure they're on the same network
2. Start the client application
3. Enter the server's IP address when prompted
4. Enter port 5000 (or custom port if changed)
5. Enter your username

## 📖 Usage Guide

### Server Commands
- **Start Server**: Run the ChatServer class
- **Stop Server**: Press `Ctrl+C` (graceful shutdown)
- **Change Port**: Pass port number as argument

### Client Commands
- **Send Message**: Type your message and press Enter
- **Exit Chat**: Type `exit` and press Enter
- **View Messages**: Messages appear automatically in real-time

### Message Format
All messages are displayed with timestamp and sender:
```
[14:30:45] Alice: Hello everyone!
[14:30:52] Bob: Hi Alice!
[14:31:00] SERVER: Charlie has joined the chat!
```

## 📸 Sample Output

### Server Terminal
```
╔════════════════════════════════════════════╗
║     CHAT SERVER STARTED SUCCESSFULLY       ║
╚════════════════════════════════════════════╝
Server is listening on port: 5000
Waiting for clients to connect...

New client connected from: 192.168.1.101
Total clients: 1

User 'Alice' joined the chat
[14:30:45] Alice: Hello everyone!

New client connected from: 192.168.1.102
Total clients: 2

User 'Bob' joined the chat
[14:30:52] Bob: Hi Alice!
[14:31:05] Alice: How are you?
[14:31:10] Bob: I'm good, thanks!

User 'Alice' left the chat
Client disconnected. Total clients: 1
```

### Client Terminal (Alice)
```
╔════════════════════════════════════════════╗
║         LAN CHAT CLIENT v1.0               ║
╚════════════════════════════════════════════╝

Enter server IP address (or 'localhost'): localhost
Enter server port (default 5000): 5000
Enter your username: Alice

Connecting to server at localhost:5000...
╔════════════════════════════════════════════╗
║       CONNECTED TO CHAT SERVER             ║
╚════════════════════════════════════════════╝
Connected to chat server as Alice

You can start chatting now!
Type 'exit' to disconnect.

Hello everyone!
[14:30:45] Alice: Hello everyone!
[14:30:48] SERVER: Bob has joined the chat!
[14:30:52] Bob: Hi Alice!
How are you?
[14:31:05] Alice: How are you?
[14:31:10] Bob: I'm good, thanks!
exit

Disconnected from server.
```

### Client Terminal (Bob)
```
╔════════════════════════════════════════════╗
║         LAN CHAT CLIENT v1.0               ║
╚════════════════════════════════════════════╝

Enter server IP address (or 'localhost'): 192.168.1.100
Enter server port (default 5000): 5000
Enter your username: Bob

Connecting to server at 192.168.1.100:5000...
╔════════════════════════════════════════════╗
║       CONNECTED TO CHAT SERVER             ║
╚════════════════════════════════════════════╝
Connected to chat server as Bob

You can start chatting now!
Type 'exit' to disconnect.

[14:30:45] Alice: Hello everyone!
Hi Alice!
[14:30:52] Bob: Hi Alice!
[14:31:05] Alice: How are you?
I'm good, thanks!
[14:31:10] Bob: I'm good, thanks!
[14:31:15] SERVER: Alice has left the chat.
```

## 🔧 Troubleshooting

### Server Issues

#### Port Already in Use
```
ERROR: PORT IN USE
Port 5000 is already in use.
```
**Solution:**
- Use a different port: `java -cp bin server.ChatServer 6000`
- Close the application using port 5000
- Wait a few moments and try again

#### Permission Denied
**Solution:**
- Use a port number above 1024
- Run with administrator privileges (not recommended)

### Client Issues

#### Connection Refused
```
ERROR: CONNECTION REFUSED
Could not connect to server at 192.168.1.100:5000
```
**Solution:**
1. Verify server is running
2. Check IP address is correct
3. Ensure port number matches server
4. Check firewall settings
5. Verify both machines are on same network

#### Invalid Server Address
```
ERROR: INVALID SERVER ADDRESS
Could not find server: 192.168.1.999
```
**Solution:**
- Verify IP address format
- Use `localhost` for same machine
- Check network connectivity

#### Server Disconnected
```
CONNECTION TO SERVER LOST
The server has disconnected.
```
**Solution:**
- Server may have crashed or shut down
- Restart the client and reconnect

### Network Issues

#### Cannot Find Server on LAN
**Solution:**
1. Verify both machines on same Wi-Fi/network
2. Check firewall isn't blocking Java
3. Ping server IP: `ping 192.168.1.100`
4. Disable VPN if active

#### Messages Not Appearing
**Solution:**
1. Check network connection
2. Restart both server and client
3. Verify no firewall blocking

## 📁 Project Structure

```
d:\Java Mini Project\
│
├── src\
│   ├── common\
│   │   ├── Message.java              # Message data encapsulation
│   │   └── MessageBroadcaster.java   # Broadcasting interface
│   │
│   ├── server\
│   │   ├── ChatServer.java           # Main server class
│   │   └── ClientHandler.java        # Client handler thread
│   │
│   └── client\
│       ├── ChatClient.java           # Main client class
│       └── MessageReceiver.java      # Message receiver thread
│
├── bin\                              # Compiled .class files
│
├── README.md                         # This file
└── VIVA_NOTES.md                     # Viva preparation notes
```

## 📚 Additional Resources

- **Viva Preparation**: See [VIVA_NOTES.md](VIVA_NOTES.md) for detailed explanations
- **Java Documentation**: https://docs.oracle.com/javase/8/docs/
- **Socket Programming**: https://docs.oracle.com/javase/tutorial/networking/

## 👨‍💻 Author

Created for Object-Oriented Programming in Java course demonstration.

## 📄 License

This project is created for educational purposes.

---

**Note**: This is a terminal-based application. No GUI is provided. All interaction happens through command-line interface.
