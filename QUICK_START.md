# Quick Start Guide - Updated with GUI Mode

## ⚡ NEW: Application Launcher

### Start with the Launcher (Recommended)
```powershell
cd "d:\Java Mini Project"
java -cp bin launcher.ApplicationLauncher
```

This opens a graphical launcher where you can choose:
1. **Interface Mode**: GUI 🖥️ or Terminal ⌨️
2. **Role**: Server 🖧 or Client 💬

---

## 🖥️ GUI Mode Quick Start

### GUI Server
1. Launch: `java -cp bin launcher.ApplicationLauncher`
2. Click **GUI Mode**
3. Click **Server**
4. Click **Start Server** (port 5000 default)
5. Monitor clients and messages in the window

### GUI Client
1. Launch: `java -cp bin launcher.ApplicationLauncher`
2. Click **GUI Mode**
3. Click **Client**
4. Enter server IP (localhost for same machine)
5. Enter username
6. Click **Connect**
7. Start chatting!

---

## ⌨️ Terminal Mode Quick Start

### Terminal Server
```powershell
java -cp bin server.ChatServer
```

### Terminal Client
```powershell
java -cp bin client.ChatClient
```

---

## 🎯 Which Mode to Choose?

### Choose GUI Mode if you want:
- ✅ Modern graphical interface
- ✅ Point-and-click controls
- ✅ Visual user list
- ✅ Easy for beginners
- ✅ Great for demos

### Choose Terminal Mode if you want:
- ✅ Lightweight interface
- ✅ Keyboard-only control
- ✅ Colorful ANSI output
- ✅ Power user experience
- ✅ Works on any terminal

---

## 🌐 LAN Testing

### Find Your IP (Windows)
```powershell
ipconfig
```
Look for "IPv4 Address" (e.g., 192.168.1.100)

### Server Machine
1. Start server (GUI or Terminal)
2. Note your IP address

### Client Machines
1. Start client (GUI or Terminal)
2. Enter server's IP address
3. Connect and chat!

---

## 📁 Project Files

```
d:\Java Mini Project\
├── src\
│   ├── common\         # Shared utilities
│   ├── server\         # Server logic
│   ├── client\         # Client logic
│   ├── launcher\       # NEW: Application launcher
│   └── gui\            # NEW: GUI components
├── bin\                # Compiled classes
├── README.md           # Full documentation
├── GUI_GUIDE.md        # NEW: GUI usage guide
├── COLOR_GUIDE.md      # Terminal colors guide
├── VIVA_NOTES.md       # Viva preparation
└── QUICK_START.md      # This file
```

---

## 🔧 Compilation

```powershell
cd "d:\Java Mini Project"
javac -d bin src/common/*.java src/server/*.java src/client/*.java src/launcher/*.java src/gui/*.java
```

---

## 🎓 For Academic Demo

### Impressive Demo Flow:
1. **Show Launcher** - Explain dual-mode design
2. **Demo GUI Mode** - Visual, modern interface
3. **Demo Terminal Mode** - Colorful, technical
4. **Show Interoperability** - GUI client with Terminal server
5. **Explain OOP** - Point to specific classes

---

## ✅ Testing Checklist

- [ ] Launcher opens and displays correctly
- [ ] GUI server starts and stops
- [ ] GUI client connects and sends messages
- [ ] Terminal server works as before
- [ ] Terminal client works as before
- [ ] GUI and Terminal modes can interoperate
- [ ] Colors display correctly in both modes

---

**Ready to impress with dual-mode chat! 🚀**
