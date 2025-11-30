package com.classes.networking;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.classes.DTO.*;

public class NetworkManager {
    private static final int PORT = 12345;
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private BlockingQueue<GameMessage> messageQueue;
    private boolean isHost = false;
    private boolean connected = false;
    
    public NetworkManager() {
        this.messageQueue = new LinkedBlockingQueue<>();
    }
    
    /**
     * Inicia como Host (Servidor)
     */
    public boolean startAsHost() {
        try {
            serverSocket = new ServerSocket(PORT);
            isHost = true;
            System.out.println("🎮 Servidor iniciado na porta " + PORT);
            
            // Aceitar conexão em thread separada
            new Thread(this::acceptConnection).start();
            return true;
        } catch (IOException e) {
            System.err.println("❌ Erro ao iniciar servidor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Conecta como Cliente
     */
    public boolean connectAsClient(String hostAddress) {
        try {
            clientSocket = new Socket(hostAddress, PORT);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            connected = true;
            
            // Iniciar thread para receber mensagens
            new Thread(this::receiveMessages).start();
            return true;
        } catch (IOException e) {
            System.err.println("❌ Erro ao conectar: " + e.getMessage());
            return false;
        }
    }
    
    private void acceptConnection() {
        try {
            System.out.println("⏳ Aguardando jogador 2...");
            clientSocket = serverSocket.accept();
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            connected = true;
            
            System.out.println("✅ Jogador 2 conectado!");
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            System.err.println("❌ Erro ao aceitar conexão: " + e.getMessage());
        }
    }
    
    private void receiveMessages() {
        try {
            while (connected) {
                GameMessage message = (GameMessage) inputStream.readObject();
                messageQueue.put(message);
                System.out.println("📨 Mensagem recebida: " + message.getType());
            }
        } catch (Exception e) {
            System.err.println("❌ Conexão perdida: " + e.getMessage());
            connected = false;
        }
    }
    
    /**
     * Envia mensagem para o outro jogador
     */
    public void sendMessage(GameMessage message) {
        if (connected && outputStream != null) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                System.err.println("❌ Erro ao enviar mensagem: " + e.getMessage());
            }
        }
    }
    
    /**
     * Pega próxima mensagem da fila (bloqueante)
     */
    public GameMessage getNextMessage() throws InterruptedException {
        return messageQueue.take();
    }
    
    /**
     * Verifica se há mensagens disponíveis
     */
    public boolean hasMessages() {
        return !messageQueue.isEmpty();
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public boolean isHost() {
        return isHost;
    }
    
    public void disconnect() {
        connected = false;
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }
}