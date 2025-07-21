# Console-Based Multiplayer XO Game using Java Sockets (Client–Server)

This project is a terminal-based multiplayer Tic-Tac-Toe (XO) game developed in Java using socket programming. It follows a client–server architecture where two players connect to a server over TCP, take turns playing on a 20x20 board, and aim to align 5 identical symbols (`X` or `O`) in a row to win the game.

## Infos
- Text-based gameplay via the terminal
- Two-player mode over a network using sockets
- Server handles game logic and board state
- Clients interact with the server and display the board
- Input validation and win detection (horizontal, vertical, diagonal)
- Detects disconnection and ends game gracefully

## What was used
- Java (JDK 8+)
- Java Sockets (`java.net.Socket`, `ServerSocket`)
- I/O Streams (`BufferedReader`, `PrintWriter`)
- Multi-threading is not used, but handled sequentially for two players

## How to Run the Game
- Open Netbeans and Right click on XOGameServer.java and click run file
- Then, Do the same two times for XOGameClient.java (because we have two players)

## Author
Maroua Boumchich
