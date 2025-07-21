/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.xogame;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class XOGameServer {
    private static final int BOARD_SIZE = 20;
    private static final char EMPTY = '-';
    private static final char[] PLAYERS = {'X', 'O'};
    private char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private int currentPlayer = 0;

    public XOGameServer() {
        for (char[] row : board) {
            Arrays.fill(row, EMPTY);
        }
    }

    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            Socket player1 = serverSocket.accept();
            System.out.println("Player X connected");
            Socket player2 = serverSocket.accept();
            System.out.println("Player O connected");

            BufferedReader[] readers = {
                new BufferedReader(new InputStreamReader(player1.getInputStream())),
                new BufferedReader(new InputStreamReader(player2.getInputStream()))
            };
            PrintWriter[] writers = {
                new PrintWriter(player1.getOutputStream(), true),
                new PrintWriter(player2.getOutputStream(), true)
            };

            writers[0].println("Welcome Player X!");
            writers[1].println("Welcome Player O!");

            boolean gameRunning = true;

            while (gameRunning) {
                PrintWriter currentWriter = writers[currentPlayer];
                BufferedReader currentReader = readers[currentPlayer];
                currentWriter.println("Your turn, Player " + PLAYERS[currentPlayer] + ". Enter row and column (e.g., 5 5):");

                try {
                    String input = currentReader.readLine();
                    if (input == null) {
                        System.out.println("Player " + PLAYERS[currentPlayer] + " disconnected.");
                        writers[1 - currentPlayer].println("The other player disconnected. Game over.");
                        break;
                    }

                    String[] parts = input.split(" ");
                    if (parts.length != 2) {
                        currentWriter.println("Invalid input format. Please enter row and column.");
                        continue;
                    }

                    int row, col;
                    try {
                        row = Integer.parseInt(parts[0]);
                        col = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        currentWriter.println("Invalid input. Please enter numbers.");
                        continue;
                    }

                    if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != EMPTY) {
                        currentWriter.println("Invalid move. Try again.");
                        continue;
                    }

                    board[row][col] = PLAYERS[currentPlayer];
                    displayBoard(writers);

                    if (checkWin(row, col)) {
                        writers[currentPlayer].println("You win! BINGO!");
                        writers[1 - currentPlayer].println("Player " + PLAYERS[currentPlayer] + " wins! BINGO!");
                        gameRunning = false;
                    } else {
                        currentPlayer = 1 - currentPlayer;
                    }

                } catch (IOException e) {
                    System.out.println("Error with player " + PLAYERS[currentPlayer] + ": " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private void displayBoard(PrintWriter[] writers) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char cell : row) {
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        for (PrintWriter writer : writers) {
            writer.println(sb.toString());
        }
    }

    private boolean checkWin(int row, int col) {
        char player = board[row][col];
        return countInDirection(row, col, 0, 1, player) + countInDirection(row, col, 0, -1, player) >= 4 ||
               countInDirection(row, col, 1, 0, player) + countInDirection(row, col, -1, 0, player) >= 4 ||
               countInDirection(row, col, 1, 1, player) + countInDirection(row, col, -1, -1, player) >= 4 ||
               countInDirection(row, col, 1, -1, player) + countInDirection(row, col, -1, 1, player) >= 4;
    }

    private int countInDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        while (true) {
            row += dRow;
            col += dCol;
            if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != player) {
                break;
            }
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        XOGameServer server = new XOGameServer();
        server.startServer(12345);
    }
}


