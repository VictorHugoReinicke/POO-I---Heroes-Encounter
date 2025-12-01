package com.classes.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkManager {
    private static final int[] PORTS = {12345, 12346, 12347, 12348, 12349}; // Múltiplas portas para tentar
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean isHost = false;
    private int currentPort;
    
    public boolean startAsHost() {
        for (int port : PORTS) {
            try {
                serverSocket = new ServerSocket(port);
                this.currentPort = port;
                isHost = true;
                System.out.println("Host iniciado na porta " + port + ". Aguardando jogador 2... IP: " + getLocalIP());
                
                // Aguardar conexão imediatamente
                System.out.println("Aguardando conexão na porta " + port + "...");
                socket = serverSocket.accept();
                initializeStreams();
                
                System.out.println("Jogador 2 conectado!");
                return true;
                
            } catch (IOException e) {
                System.out.println("Porta " + port + " ocupada, tentando próxima...");
                continue; // Tenta a próxima porta
            }
        }
        
        System.err.println("Todas as portas estão ocupadas!");
        return false;
    }
    
    public boolean connectAsClient(String hostIP) {
        // Tenta conectar nas portas possíveis
        for (int port : PORTS) {
            try {
                System.out.println("Tentando conectar em " + hostIP + ":" + port);
                socket = new Socket(hostIP, port);
                this.currentPort = port;
                isHost = false;
                initializeStreams();
                System.out.println("Conectado com sucesso na porta " + port);
                return true;
                
            } catch (IOException e) {
                System.out.println("Falha na porta " + port + ", tentando próxima...");
                continue;
            }
        }
        
        System.err.println("Não foi possível conectar em nenhuma porta!");
        return false;
    }
    
    private void initializeStreams() throws IOException {
        if (socket != null && socket.isConnected()) {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams inicializados com sucesso");
        }
    }
    
    public void sendObject(Object obj) {
        try {
            if (output != null) {
                output.writeObject(obj);
                output.flush();
                System.out.println("Objeto enviado: " + obj.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar objeto: " + e.getMessage());
        }
    }
    
    public Object receiveObject() {
        try {
            if (input != null) {
                Object obj = input.readObject();
                System.out.println("Objeto recebido: " + (obj != null ? obj.getClass().getSimpleName() : "null"));
                return obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao receber objeto: " + e.getMessage());
        }
        return null;
    }
    
    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
            System.out.println("Conexão fechada");
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
    
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    public int getCurrentPort() {
        return currentPort;
    }
}