package com.classes.networking;

import com.classes.DTO.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkManager {
    private static final int PORT = 12345;
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private BlockingQueue<GameMessage> messageQueue;
    private boolean isHost = false;
    private boolean connected = false;
    private Thread receiveThread;
    
    public NetworkManager() {
        this.messageQueue = new LinkedBlockingQueue<>();
    }
    
    /**
     * Inicia como Host (Servidor)
     */
    public boolean startAsHost() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
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
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(hostAddress, PORT), 5000);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            connected = true;
            
            System.out.println("✅ Conectado ao host: " + hostAddress);
            
            // Iniciar thread para receber mensagens
            startReceivingThread();
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
            startReceivingThread();
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao aceitar conexão: " + e.getMessage());
        }
    }
    
    private void startReceivingThread() {
        receiveThread = new Thread(this::receiveMessages);
        receiveThread.setDaemon(true);
        receiveThread.start();
    }
    
    private void receiveMessages() {
        try {
            while (connected && !Thread.currentThread().isInterrupted()) {
                Object obj = inputStream.readObject();
                if (obj instanceof GameMessage) {
                    GameMessage message = (GameMessage) obj;
                    messageQueue.put(message);
                    System.out.println("📨 Mensagem recebida: " + message.getType());
                }
            }
        } catch (EOFException e) {
            System.out.println("🔌 Conexão fechada pelo outro jogador");
        } catch (Exception e) {
            if (connected) {
                System.err.println("❌ Erro ao receber mensagem: " + e.getMessage());
            }
        } finally {
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
                System.out.println("📤 Mensagem enviada: " + message.getType());
            } catch (IOException e) {
                System.err.println("❌ Erro ao enviar mensagem: " + e.getMessage());
                connected = false;
            }
        }
    }
    
    /**
     * Sincroniza jogador após conexão
     */
    public void sincronizarJogador(Jogador jogador) {
        GameMessage syncMsg = new GameMessage(
            GameMessage.MessageType.PLAYER_SYNC,
            jogador,
            jogador.getId()
        );
        sendMessage(syncMsg);
    }
    
    /**
     * ✅ SINCRONIZA PERSONAGEM DO HOST PARA O CLIENT
     */
    public void sincronizarPersonagemHost(Jogador jogadorHost) {
        Jogador jogadorSincronizado = clonarJogador(jogadorHost);
        
        GameMessage syncMsg = new GameMessage(
            GameMessage.MessageType.PLAYER_SYNC,
            jogadorSincronizado,
            jogadorHost.getId()
        );
        sendMessage(syncMsg);
    }
    
    /**
     * ✅ CLONE SEGURO DO JOGADOR PARA EVITAR PROBLEMAS DE REFERÊNCIA
     */
    private Jogador clonarJogador(Jogador original) {
        return Multiplayer.criarJogadorParaMultiplayer(original);
    }
    
   
    /**
     * Pega próxima mensagem da fila (não bloqueante)
     */
    public GameMessage getNextMessage() {
        return messageQueue.poll();
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
            if (receiveThread != null) {
                receiveThread.interrupt();
            }
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }
}