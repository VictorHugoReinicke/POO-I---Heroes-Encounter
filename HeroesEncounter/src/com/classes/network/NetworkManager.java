package com.classes.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class NetworkManager {

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final BlockingQueue<Object> receivedQueue = new LinkedBlockingQueue<>();

    private Thread listenThread;
    private boolean running = false;

    // Porta atual do host ou client
    private int currentPort = 0;

    // Faixa de portas permitidas
    private final int PORT_START = 5000;
    private final int PORT_END = 5010;

    // Timeout de segurança
    private final int SOCKET_TIMEOUT = 8000;

    public int getCurrentPort() {
        return currentPort;
    }

    // ============================================================
    //                  INICIAR COMO HOST
    // ============================================================
    public boolean startAsHost() {
        if (running) stop();

        for (int port = PORT_START; port <= PORT_END; port++) {
            try {
                serverSocket = new ServerSocket(port);
                currentPort = port;

                System.out.println("[HOST] Aguardando conexão na porta " + port + "...");
                socket = serverSocket.accept();
                socket.setSoTimeout(SOCKET_TIMEOUT);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                startListenerThread();
                running = true;
                return true;

            } catch (IOException e) {
                // Porta ocupada, tenta a próxima
            }
        }

        return false; // todas as portas ocupadas
    }

    // ============================================================
    //                  INICIAR COMO CLIENTE
    // ============================================================
    public boolean startAsClient(String ip) {
        if (running) stop();

        for (int port = PORT_START; port <= PORT_END; port++) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), SOCKET_TIMEOUT);
                socket.setSoTimeout(SOCKET_TIMEOUT);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                currentPort = port;
                startListenerThread();
                running = true;

                return true;

            } catch (IOException e) {
                // tenta a próxima porta
            }
        }

        return false;
    }

    // ============================================================
    //                  THREAD OUVINTE
    // ============================================================
    private void startListenerThread() {
        listenThread = new Thread(() -> {
            try {
                while (!Thread.interrupted() && socket != null && !socket.isClosed()) {
                    try {
                        Object obj = in.readObject();
                        receivedQueue.offer(obj); // coloca na fila
                    } catch (SocketTimeoutException timeout) {
                        // timeout normal → continua
                    }
                }
            } catch (Exception e) {
                System.err.println("[ERRO LISTENER] " + e.getMessage());
            }
        });

        listenThread.setDaemon(true);
        listenThread.start();
    }

    // ============================================================
    //                  RECEBER OBJETO (BLOQUEANTE)
    // ============================================================
    public Object receiveObject() throws Exception {
        return receivedQueue.take();
    }

    // ============================================================
    //                  ENVIAR OBJETO
    // ============================================================
    public synchronized void sendObject(Object obj) throws Exception {
        if (out != null) {
            out.writeObject(obj);
            out.flush();
        }
    }

    // ============================================================
    //                  FECHAR CONEXÃO
    // ============================================================
    public void stop() {
        running = false;
        try { if (listenThread != null) listenThread.interrupt(); } catch (Exception ignored) {}
        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception ignored) {}
    }
    
    public void sendObjectSafe(Object obj) {
        try {
            sendObject(obj);
        } catch (Exception e) {
            System.err.println("[ERRO AO ENVIAR] " + e.getMessage());
        }
    }
}
