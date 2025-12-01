package com.classes.network;

import java.io.*;
import java.net.*;
import java.util.*;
import com.classes.DTO.Jogador;

public class NetworkManager {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean isHost = false;
    
    public boolean startAsHost() {
        try {
            serverSocket = new ServerSocket(PORT);
            isHost = true;
            System.out.println("Host iniciado. Aguardando jogador 2... IP: " + getLocalIP());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean connectAsClient(String hostIP) {
        try {
            clientSocket = new Socket(hostIP, PORT);
            isHost = false;
            initializeStreams();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Socket waitForClient() {
        try {
            Socket client = serverSocket.accept();
            initializeStreams();
            return client;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void initializeStreams() throws IOException {
        if (isHost) {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } else {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        }
    }
    
    public void sendObject(Object obj) {
        try {
            output.writeObject(obj);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Object receiveObject() {
        try {
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
    
    public boolean isHost() {
        return isHost;
    }
}