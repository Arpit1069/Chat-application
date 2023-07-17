package Server;

import Client.Controller;
import Client.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.*;
public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Connection conn;
    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients,Connection conn) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.conn = conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
    
                // Get the current time
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(currentTimeMillis);
    
                // Parse username and message from the received message
                String[] parts = msg.split(":");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String message = parts[1];
    
                    // Insert username, message, and timestamp into database
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO message (sender, message, timestamp) VALUES (?, ?, ?)");
                    stmt.setString(1, username);
                    stmt.setString(2, message);
                    stmt.setTimestamp(3, timestamp);
                    stmt.executeUpdate();
    
                    // Send message and timestamp to all connected clients
                    String messageWithTimestamp = username + ":" + message + "            (" + timestamp.toString() + ")";
                    for (ClientHandler cl : clients) {
                        cl.writer.println(messageWithTimestamp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}

    // public void run() {
    //     try {
    //         String msg;
    //         while ((msg = reader.readLine()) != null) {
    //             if (msg.equalsIgnoreCase( "exit")) {
    //                 break;
    //             }
    //             PreparedStatement stmt = conn.prepareStatement("INSERT INTO message (sender, message) VALUES (?, ?)");
    //             stmt.setString(1, socket.getInetAddress().getHostName());
    //             stmt.setString(2, msg);
                          
    //             stmt.executeUpdate();
    //             for (ClientHandler cl : clients) {
    //                 cl.writer.println(msg);
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     finally {
    //         try {
    //             reader.close();
    //             writer.close();
    //             socket.close();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }

    // }