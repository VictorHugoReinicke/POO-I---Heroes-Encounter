package com.classes.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkManager {

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final BlockingQueue<Object> receivedQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private Thread listenThread;
    private Thread heartbeatThread;

    private int currentPort = 0;
    private final int PORT_START = 5000;
    private final int PORT_END = 5010;
    private final int SOCKET_TIMEOUT = 8000;
    private final int HEARTBEAT_INTERVAL = 3000;

    public int getCurrentPort() {
        return currentPort;
    }

    public boolean isConnected() {
        return running.get() && socket != null && socket.isConnected() && !socket.isClosed();
    }

    // ============================================================
    //                  INICIAR COMO HOST
    // ============================================================
    public boolean startAsHost() {
        if (running.get()) stop();

        for (int port = PORT_START; port <= PORT_END; port++) {
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(10000); // Timeout para accept
                currentPort = port;

                System.out.println("[HOST] Aguardando conexão na porta " + port + "...");
                
                // Thread para aceitar conexão sem bloquear a UI
                CompletableFuture<Socket> socketFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return serverSocket.accept();
                    } catch (IOException e) {
                        return null;
                    }
                });

                socket = socketFuture.get(10, TimeUnit.SECONDS); // Timeout de 10 segundos
                
                if (socket == null) {
                    serverSocket.close();
                    continue;
                }

                setupSocket();
                startListenerThread();
                startHeartbeat();
                running.set(true);
                return true;

            } catch (Exception e) {
                System.err.println("[HOST] Erro na porta " + port + ": " + e.getMessage());
                closeSocketAndServer();
            }
        }
        return false;
    }

    // ============================================================
    //                  INICIAR COMO CLIENTE
    // ============================================================
    public boolean startAsClient(String ip) {
        if (running.get()) stop();

        for (int port = PORT_START; port <= PORT_END; port++) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), SOCKET_TIMEOUT);
                
                setupSocket();
                currentPort = port;
                startListenerThread();
                startHeartbeat();
                running.set(true);
                return true;

            } catch (IOException e) {
                System.err.println("[CLIENT] Porta " + port + " falhou: " + e.getMessage());
                closeSocket();
            }
        }
        return false;
    }

    // ============================================================
    //                  CONFIGURAÇÃO DO SOCKET
    // ============================================================
    private void setupSocket() throws IOException {
        socket.setSoTimeout(SOCKET_TIMEOUT);
        socket.setTcpNoDelay(true); // Melhor performance para pequenos pacotes
        
        // IMPORTANTE: Criar OutputStream antes do InputStream para evitar deadlock
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // Garantir que header seja enviado
        in = new ObjectInputStream(socket.getInputStream());
        
        System.out.println("[NETWORK] Conexão estabelecida com sucesso");
    }

    // ============================================================
    //                  THREAD OUVINTE
    // ============================================================
    private void startListenerThread() {
        listenThread = new Thread(() -> {
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    Object obj = in.readObject();
                    if ("HEARTBEAT".equals(obj)) {
                        // Ignorar heartbeats, apenas manter conexão
                        continue;
                    }
                    receivedQueue.offer(obj);
                    System.out.println("[NETWORK] Objeto recebido: " + obj.getClass().getSimpleName());
                } catch (SocketTimeoutException e) {
                    // Timeout normal, continuar ouvindo
                    continue;
                } catch (EOFException e) {
                    System.err.println("[LISTENER] Conexão fechada pelo peer");
                    break;
                } catch (IOException e) {
                    if (running.get()) {
                        System.err.println("[LISTENER] Erro de IO: " + e.getMessage());
                    }
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("[LISTENER] Classe não encontrada: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("[LISTENER] Erro inesperado: " + e.getMessage());
                    break;
                }
            }
            
            // Conexão fechada
            if (running.get()) {
                System.err.println("[LISTENER] Conexão perdida");
                stop();
            }
        });

        listenThread.setDaemon(true);
        listenThread.start();
    }

    // ============================================================
    //                  HEARTBEAT PARA MANTER CONEXÃO
    // ============================================================
    private void startHeartbeat() {
        heartbeatThread = new Thread(() -> {
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    if (isConnected()) {
                        sendObject("HEARTBEAT");
                    }
                } catch (Exception e) {
                    System.err.println("[HEARTBEAT] Erro: " + e.getMessage());
                    break;
                }
            }
        });
        
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }

    // ============================================================
    //                  RECEBER OBJETO (BLOQUEANTE)
    // ============================================================
    public Object receiveObject() throws InterruptedException {
        return receivedQueue.take();
    }

    public Object receiveObjectWithTimeout(long timeout, TimeUnit unit) throws InterruptedException {
        return receivedQueue.poll(timeout, unit);
    }

    // ============================================================
    //                  ENVIAR OBJETO
    // ============================================================
    public synchronized void sendObject(Object obj) throws IOException {
        if (out != null && isConnected()) {
            out.writeObject(obj);
            out.flush();
            out.reset(); // Important: reset stream to avoid caching issues
            System.out.println("[NETWORK] Objeto enviado: " + obj.getClass().getSimpleName());
        } else {
            throw new IOException("Conexão não disponível para envio");
        }
    }

    // ============================================================
    //                  FECHAR CONEXÃO
    // ============================================================
    public void stop() {
        running.set(false);
        
        if (listenThread != null) {
            listenThread.interrupt();
        }
        if (heartbeatThread != null) {
            heartbeatThread.interrupt();
        }
        
        closeSocketAndServer();
        receivedQueue.clear();
        
        System.out.println("[NETWORK] Conexão fechada");
    }

    private void closeSocketAndServer() {
        closeSocket();
        closeServerSocket();
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("[NETWORK] Erro ao fechar socket: " + e.getMessage());
        } finally {
            socket = null;
            in = null;
            out = null;
        }
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[NETWORK] Erro ao fechar server socket: " + e.getMessage());
        } finally {
            serverSocket = null;
        }
    }
    
    public void sendObjectSafe(Object obj) {
        try {
            sendObject(obj);
        } catch (Exception e) {
            System.err.println("[ERRO AO ENVIAR] " + e.getMessage());
            stop();
        }
    }
}