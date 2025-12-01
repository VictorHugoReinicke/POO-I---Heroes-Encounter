package com.classes.main;

import com.classes.networking.*;
import com.classes.DTO.*;
import com.classes.BO.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class TelaAventuraMultiplayer extends JFrame {
    private Jogador jogadorLocal;
    private Jogador jogadorRemoto;
    private NetworkManager networkManager;
    private SessaoMultiplayer sessao;
    private int batalhasVencidas;
    private boolean jornadaAtiva;
    private InimigoBO inimigoBO;
    private Random random;

    // Componentes da interface
    private JLabel lblStatus;
    private JLabel lblProgresso;
    private JLabel lblJogadorLocal;
    private JLabel lblJogadorRemoto;
    private JTextArea txtLog;
    private JButton btnIniciarJornada;
    private JButton btnProximaBatalha;
    private JButton btnStatus;
    private JButton btnVoltarMenu;

    // ✅ CONSTRUTOR PARA USO DA TELA AVENTURA ORIGINAL
    public TelaAventuraMultiplayer(Jogador jogador) {
        this(jogador, new NetworkManager(), SessaoMultiplayer.getInstancia());
    }

    // ✅ CONSTRUTOR PARA USO DA TELA MULTIPLAYER PRINCIPAL
    public TelaAventuraMultiplayer(Jogador jogador, NetworkManager networkManager, SessaoMultiplayer sessao) {
        this.jogadorLocal = jogador;
        this.networkManager = networkManager;
        this.sessao = sessao;
        this.batalhasVencidas = 0;
        this.jornadaAtiva = false;
        this.inimigoBO = new InimigoBO();
        this.random = new Random();
        
        // ✅ TENTA OBTER JOGADOR REMOTO DA SESSÃO
        this.jogadorRemoto = obterJogadorRemoto();
        
        initializeTela();
        iniciarProcessamentoMensagens();
        
        // ✅ SE VEIO DA TELA AVENTURA ORIGINAL, INICIA CONEXÃO AUTOMÁTICA
        if (this.networkManager.isConnected()) {
            adicionarLog("🔗 Conectado via sessão existente");
        } else {
            adicionarLog("🎮 Modo multiplayer standalone");
        }
    }

    private Jogador obterJogadorRemoto() {
        List<Jogador> todosJogadores = sessao.getTodosJogadores();
        for (Jogador j : todosJogadores) {
            if (j.getId() != jogadorLocal.getId()) {
                return j;
            }
        }
        return null;
    }

    private void initializeTela() {
        setTitle("Aventura Multiplayer - " + jogadorLocal.getNome());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("AVENTURA MULTIPLAYER - " + jogadorLocal.getNome().toUpperCase(), JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // Painel de status multiplayer
        JPanel statusPanel = new JPanel(new GridLayout(4, 1));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusPanel.setBackground(Color.WHITE);

        lblStatus = new JLabel("Status: " + (networkManager.isConnected() ? 
            (networkManager.isHost() ? "🏠 Host" : "🔗 Client") : "🔴 Desconectado"));
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

        lblJogadorLocal = new JLabel("🎮 Você: " + jogadorLocal.getNome() + " | HP: " + jogadorLocal.getHp() + "/" + jogadorLocal.getHpMax());
        lblJogadorLocal.setFont(new Font("Arial", Font.BOLD, 12));

        if (jogadorRemoto != null) {
            lblJogadorRemoto = new JLabel("👥 Parceiro: " + jogadorRemoto.getNome() + " | HP: " + jogadorRemoto.getHp() + "/" + jogadorRemoto.getHpMax());
        } else {
            lblJogadorRemoto = new JLabel("👥 Parceiro: Aguardando conexão...");
        }
        lblJogadorRemoto.setFont(new Font("Arial", Font.BOLD, 12));
        lblJogadorRemoto.setForeground(Color.BLUE);

        lblProgresso = new JLabel("Progresso: Jornada não iniciada");
        lblProgresso.setFont(new Font("Arial", Font.BOLD, 12));

        statusPanel.add(lblStatus);
        statusPanel.add(lblJogadorLocal);
        statusPanel.add(lblJogadorRemoto);
        statusPanel.add(lblProgresso);

        // Painel de log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log da Aventura Cooperativa"));

        txtLog = new JTextArea(15, 50);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLog.setEditable(false);
        txtLog.setBackground(Color.BLACK);
        txtLog.setForeground(Color.WHITE);

        JScrollPane scrollLog = new JScrollPane(txtLog);
        logPanel.add(scrollLog, BorderLayout.CENTER);

        // Painel de ações
        JPanel acoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        acoesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        acoesPanel.setBackground(new Color(240, 240, 240));

        btnIniciarJornada = criarBotaoAventura("⚔️ INICIAR JORNADA", new Color(220, 60, 60));
        btnProximaBatalha = criarBotaoAventura("🎯 PRÓXIMA BATALHA", new Color(60, 120, 220));
        btnStatus = criarBotaoAventura("📊 VER STATUS", new Color(60, 180, 120));
        btnVoltarMenu = criarBotaoAventura("🏠 VOLTAR", new Color(100, 100, 100));

        btnIniciarJornada.addActionListener(e -> iniciarJornadaMultiplayer());
        btnProximaBatalha.addActionListener(e -> proximaBatalhaMultiplayer());
        btnStatus.addActionListener(e -> mostrarStatusMultiplayer());
        btnVoltarMenu.addActionListener(e -> voltar());

        acoesPanel.add(btnIniciarJornada);
        acoesPanel.add(btnProximaBatalha);
        acoesPanel.add(btnStatus);
        acoesPanel.add(btnVoltarMenu);

        // Inicialmente desabilitar próximo batalha
        btnProximaBatalha.setEnabled(false);

        add(tituloPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(acoesPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null);

        // Mensagem de boas-vindas
        adicionarLog("🎮 BEM-VINDO AO MODO MULTIPLAYER!");
        adicionarLog("🤝 " + (networkManager.isConnected() ? 
            "Conectado como " + (networkManager.isHost() ? "HOST" : "CLIENT") : 
            "Modo Singleplayer Multiplayer"));
        adicionarLog("🎮 Seu personagem: " + jogadorLocal.getNome());
        
        if (jogadorRemoto != null) {
            adicionarLog("👥 Parceiro: " + jogadorRemoto.getNome());
            adicionarLog("✅ Pronto para aventura cooperativa!");
        } else if (networkManager.isConnected()) {
            adicionarLog("⏳ Aguardando parceiro conectar...");
        } else {
            adicionarLog("💡 Dica: Use o menu principal para conectar com outro jogador");
        }
        
        adicionarLog("");
        adicionarLog("Clique em 'INICIAR JORNADA' para começar!");
    }

    private JButton criarBotaoAventura(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(180, 50));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
                botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
                botao.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return botao;
    }

    private void iniciarJornadaMultiplayer() {
        if (!jornadaAtiva) {
            jornadaAtiva = true;
            batalhasVencidas = 0;

            adicionarLog("==========================================");
            adicionarLog("⚔️ JORNADA COOPERATIVA INICIADA! ⚔️");
            adicionarLog("==========================================");
            adicionarLog("Dupla: " + jogadorLocal.getNome() + " & " + 
                        (jogadorRemoto != null ? jogadorRemoto.getNome() : "Aguardando..."));
            adicionarLog("Missão: Vencer 3 batalhas épicas juntos!");
            adicionarLog("==========================================");

            lblStatus.setText("Status: Jornada Cooperativa em Andamento");
            lblProgresso.setText("Progresso: " + batalhasVencidas + "/3 batalhas vencidas");

            btnIniciarJornada.setEnabled(false);
            btnProximaBatalha.setEnabled(true);

            // ✅ SINCRONIZAR INÍCIO DE JORNADA COM OUTRO JOGADOR
            if (networkManager.isConnected()) {
                GameMessage jornadaMsg = new GameMessage(
                    GameMessage.MessageType.GAME_SYNC,
                    "JORNADA_INICIADA",
                    jogadorLocal.getId()
                );
                networkManager.sendMessage(jornadaMsg);
            }

            JOptionPane.showMessageDialog(this,
                    "⚔️ JORNADA COOPERATIVA INICIADA! ⚔️\n\n" +
                    "Dupla: " + jogadorLocal.getNome() + " & " + 
                    (jogadorRemoto != null ? jogadorRemoto.getNome() : "Parceiro") + "\n\n" +
                    "Trabalhem juntos para vencer 3 batalhas!",
                    "Jornada Cooperativa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void proximaBatalhaMultiplayer() {
        if (!jornadaAtiva) {
            JOptionPane.showMessageDialog(this, "Inicie a jornada primeiro!", "Jornada Não Iniciada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Inimigo inimigo = obterInimigoAleatorio(batalhasVencidas == 2);

            adicionarLog("");
            adicionarLog("🎯 INICIANDO BATALHA COOPERATIVA " + (batalhasVencidas + 1));
            adicionarLog("👹 Inimigo: " + inimigo.getNome());
            adicionarLog("❤️ HP: " + inimigo.getHp() + " | ⚔️ Ataque: " + inimigo.getAtaque());

            // ✅ INICIAR COMBATE MULTIPLAYER
            TelaCombateMultiplayer telaCombate = new TelaCombateMultiplayer(
                this, // ✅ Agora é JFrame
                jogadorLocal, 
                jogadorRemoto, 
                inimigo, 
                networkManager
            );
            telaCombate.setVisible(true);

        } catch (Exception e) {
            adicionarLog("❌ Erro ao iniciar batalha: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao iniciar batalha: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Inimigo obterInimigoAleatorio(boolean isChefe) {
        try {
            // ✅ APENAS HOST USA O BANCO - CLIENT RECEBE VIA REDE
            if (networkManager.isHost()) {
                List<Inimigo> todosInimigos = inimigoBO.pesquisarTodos();

                if (todosInimigos == null || todosInimigos.isEmpty()) {
                    throw new Exception("Nenhum inimigo encontrado no banco de dados");
                }

                List<Inimigo> inimigosFiltrados = new java.util.ArrayList<>();
                for (Inimigo inimigo : todosInimigos) {
                    String nomeClasse = inimigo.getClass().getSimpleName();
                    if (isChefe && nomeClasse.equalsIgnoreCase("Chefe")) {
                        inimigosFiltrados.add(inimigo);
                    } else if (!isChefe && !nomeClasse.equalsIgnoreCase("Chefe")) {
                        inimigosFiltrados.add(inimigo);
                    }
                }

                if (inimigosFiltrados.isEmpty()) {
                    inimigosFiltrados = todosInimigos;
                }

                int indexAleatorio = random.nextInt(inimigosFiltrados.size());
                Inimigo inimigoSelecionado = inimigosFiltrados.get(indexAleatorio);
                inimigoSelecionado.setHp(inimigoSelecionado.getHpMax());

                return inimigoSelecionado;

            } else {
                // ✅ CLIENT: USA INIMIGO BÁSICO (SERÁ SINCRONIZADO PELO HOST)
                adicionarLog("⏳ Aguardando host selecionar inimigo...");
                return InimigoFactory.criarInimigo(isChefe ? "Chefe" : "Besta");
            }

        } catch (Exception e) {
            adicionarLog("❌ Erro ao carregar inimigo: " + e.getMessage());
            return InimigoFactory.criarInimigo(isChefe ? "Chefe" : "Besta");
        }
    }

    public void batalhaVencida() {
        batalhasVencidas++;
        adicionarLog("🎉 Vitória Cooperativa! Batalhas vencidas: " + batalhasVencidas + "/3");
        lblProgresso.setText("Progresso: " + batalhasVencidas + "/3 batalhas vencidas");

        // ✅ SINCRONIZAR VITÓRIA
        if (networkManager.isConnected()) {
            GameMessage vitoriaMsg = new GameMessage(
                GameMessage.MessageType.GAME_SYNC,
                "VITORIA_" + batalhasVencidas,
                jogadorLocal.getId()
            );
            networkManager.sendMessage(vitoriaMsg);
        }

        if (batalhasVencidas >= 3) {
            jornadaCompleta();
        } else {
            // Restaurar recursos entre batalhas
            jogadorLocal.setHp(Math.min(jogadorLocal.getHpMax(), jogadorLocal.getHp() + 20));
            if (jogadorLocal.getManaMax() > 0) {
                jogadorLocal.setMana(jogadorLocal.getManaMax());
            }
            adicionarLog("💖 HP restaurado em 20 pontos!");
        }
    }

    public void batalhaPerdida() {
        jornadaAtiva = false;
        adicionarLog("💀 Derrota! A jornada cooperativa termina aqui...");
        lblStatus.setText("Status: Derrotado");
        lblProgresso.setText("Progresso: Jornada Falhou");

        btnProximaBatalha.setEnabled(false);

        // ✅ SINCRONIZAR DERROTA
        if (networkManager.isConnected()) {
            GameMessage derrotaMsg = new GameMessage(
                GameMessage.MessageType.GAME_SYNC,
                "DERROTA",
                jogadorLocal.getId()
            );
            networkManager.sendMessage(derrotaMsg);
        }
    }

    private void jornadaCompleta() {
        jornadaAtiva = false;

        adicionarLog("");
        adicionarLog("==========================================");
        adicionarLog("🏆 JORNADA COOPERATIVA COMPLETA! 🏆");
        adicionarLog("==========================================");
        adicionarLog("Parabéns, " + jogadorLocal.getNome() + "!");
        adicionarLog("Vocês venceram todas as 3 batalhas juntos!");
        adicionarLog("Uma dupla lendária foi formada!");
        adicionarLog("==========================================");

        lblStatus.setText("Status: Jornada Cooperativa Completa!");
        lblProgresso.setText("Progresso: MISSÃO CUMPRIDA! 🏆");

        btnProximaBatalha.setEnabled(false);
        
        JOptionPane.showMessageDialog(this,
            "🏆 JORNADA COOPERATIVA COMPLETA!\n\n" +
            "Parabéns! Vocês venceram todas as 3 batalhas!\n" +
            "Uma dupla lendária foi formada!",
            "Jornada Vitoriosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarStatusMultiplayer() {
        StringBuilder status = new StringBuilder();
        status.append("=== STATUS DA DUPLA ===\n\n");
        
        status.append("🎮 SEU PERSONAGEM:\n");
        status.append("Nome: ").append(jogadorLocal.getNome()).append("\n");
        status.append("Classe: ").append(determinarClasse(jogadorLocal)).append("\n");
        status.append("HP: ").append(jogadorLocal.getHp()).append("/").append(jogadorLocal.getHpMax()).append("\n");
        status.append("Mana: ").append(jogadorLocal.getMana()).append("/").append(jogadorLocal.getManaMax()).append("\n");
        status.append("Ataque: ").append(jogadorLocal.getAtaque()).append("\n");
        status.append("Ouro: ").append(jogadorLocal.getOuro()).append("\n\n");
        
        if (jogadorRemoto != null) {
            status.append("👥 PARCEIRO:\n");
            status.append("Nome: ").append(jogadorRemoto.getNome()).append("\n");
            status.append("Classe: ").append(determinarClasse(jogadorRemoto)).append("\n");
            status.append("HP: ").append(jogadorRemoto.getHp()).append("/").append(jogadorRemoto.getHpMax()).append("\n");
        }
        
        status.append("\nProgresso: ").append(batalhasVencidas).append("/3 batalhas\n");
        status.append("Conexão: ").append(networkManager.isConnected() ? 
            (networkManager.isHost() ? "Host" : "Client") : "Desconectado");

        JOptionPane.showMessageDialog(this, status.toString(), "Status da Dupla", JOptionPane.INFORMATION_MESSAGE);
    }

    private void voltar() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja voltar?\n" + "Sua sessão multiplayer será encerrada.",
                "Voltar", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            if (networkManager != null) {
                networkManager.disconnect();
            }
            dispose();
            
            // ✅ VOLTA PARA A TELA DE SELEÇÃO
            new TelaSelecao().setVisible(true);
        }
    }

    private void adicionarLog(String mensagem) {
        txtLog.append(mensagem + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    private void iniciarProcessamentoMensagens() {
        new Thread(() -> {
            while (networkManager != null && networkManager.isConnected()) {
                try {
                    if (networkManager.hasMessages()) {
                        GameMessage message = networkManager.getNextMessage();
                        if (message != null) {
                            SwingUtilities.invokeLater(() -> {
                                processarMensagem(message);
                            });
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    System.err.println("Erro no processamento de mensagens: " + e.getMessage());
                }
            }
        }).start();
    }

    private void processarMensagem(GameMessage message) {
        if (message == null) return;
        
        switch (message.getType()) {
            case PLAYER_SYNC:
                if (message.getData() instanceof Jogador) {
                    Jogador novoJogador = (Jogador) message.getData();
                    if (novoJogador.getId() != jogadorLocal.getId()) {
                        jogadorRemoto = novoJogador;
                        sessao.adicionarJogador(jogadorRemoto);
                        lblJogadorRemoto.setText("👥 Parceiro: " + jogadorRemoto.getNome() + 
                                                " | HP: " + jogadorRemoto.getHp() + "/" + jogadorRemoto.getHpMax());
                        adicionarLog("✅ Parceiro conectado: " + jogadorRemoto.getNome());
                        
                        // Se jornada já estava ativa, atualizar status
                        if (jornadaAtiva) {
                            adicionarLog("🤝 " + jogadorRemoto.getNome() + " juntou-se à jornada!");
                        }
                    }
                }
                break;
                
            case GAME_SYNC:
                String dados = (String) message.getData();
                adicionarLog("📊 Sincronização: " + dados);
                
                if (dados.startsWith("JORNADA_INICIADA")) {
                    if (!jornadaAtiva) {
                        jornadaAtiva = true;
                        btnIniciarJornada.setEnabled(false);
                        btnProximaBatalha.setEnabled(true);
                        adicionarLog("✅ Jornada iniciada pelo host!");
                    }
                } else if (dados.startsWith("VITORIA_")) {
                    batalhasVencidas = Integer.parseInt(dados.split("_")[1]);
                    lblProgresso.setText("Progresso: " + batalhasVencidas + "/3 batalhas vencidas");
                    adicionarLog("🎉 Parceiro registrou vitória!");
                }
                break;
                
            default:
                adicionarLog("📨 Mensagem: " + message.getType());
        }
    }

    private String determinarClasse(Jogador jogador) {
        if (jogador instanceof Guerreiro) return "Guerreiro";
        if (jogador instanceof Mago) return "Mago";
        if (jogador instanceof Paladino) return "Paladino";
        return "Desconhecida";
    }
}