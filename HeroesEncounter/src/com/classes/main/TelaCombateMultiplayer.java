package com.classes.main;

import com.classes.networking.*;
import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;

public class TelaCombateMultiplayer extends JDialog {
    private NetworkManager networkManager;
    private Jogador jogadorLocal;
    private Jogador jogadorRemoto;
    private Inimigo inimigo;
    private boolean minhaVez;
    
    // Componentes da interface (herdados/adaptados do TelaCombate)
    private JLabel lblJogadorHP;
    private JLabel lblJogadorMana;
    private JLabel lblInimigoHP;
    private JLabel lblJogadorRemotoHP;
    private JTextArea txtLog;
    private JButton btnAtaqueNormal;
    private JButton btnHabilidades;
    private JButton btnItens;
    private JButton btnFugir;
    private JButton btnDefender;

    private boolean defesaAtiva = false;

    // ✅ CONSTRUTOR CORRIGIDO
    public TelaCombateMultiplayer(JFrame pai, Jogador jogadorLocal, Jogador jogadorRemoto, Inimigo inimigo, NetworkManager networkManager) {
        super(pai, "Combate Cooperativo - " + inimigo.getNome(), true);
        this.jogadorLocal = jogadorLocal;
        this.jogadorRemoto = jogadorRemoto;
        this.inimigo = inimigo;
        this.networkManager = networkManager;
        this.minhaVez = networkManager.isHost(); // Host começa
        
        initializeTela();
        iniciarCombateMultiplayer();
    }

    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("COMBATE COOPERATIVO - " + inimigo.getNome().toUpperCase());
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // ✅ PAINEL DE STATUS MULTIPLAYER (Jogador Local + Remoto)
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusPanel.setBackground(Color.WHITE);

        // Status do Jogador Local
        JPanel jogadorLocalPanel = new JPanel(new FlowLayout());
        jogadorLocalPanel.setBackground(new Color(220, 240, 255));
        jogadorLocalPanel.setBorder(BorderFactory.createTitledBorder("🎮 " + jogadorLocal.getNome() + " (Você)"));

        lblJogadorHP = new JLabel("❤️ HP: " + jogadorLocal.getHp() + "/" + jogadorLocal.getHpMax());
        lblJogadorHP.setFont(new Font("Arial", Font.BOLD, 12));

        lblJogadorMana = new JLabel("🔵 Mana: " + jogadorLocal.getMana() + "/" + jogadorLocal.getManaMax());
        lblJogadorMana.setFont(new Font("Arial", Font.BOLD, 12));

        jogadorLocalPanel.add(lblJogadorHP);
        jogadorLocalPanel.add(lblJogadorMana);

        // Status do Jogador Remoto
        JPanel jogadorRemotoPanel = new JPanel(new FlowLayout());
        jogadorRemotoPanel.setBackground(new Color(255, 240, 220));
        jogadorRemotoPanel.setBorder(BorderFactory.createTitledBorder("👥 " + (jogadorRemoto != null ? jogadorRemoto.getNome() : "Parceiro")));

        lblJogadorRemotoHP = new JLabel("❤️ HP: " + 
            (jogadorRemoto != null ? jogadorRemoto.getHp() + "/" + jogadorRemoto.getHpMax() : "Conectando..."));
        lblJogadorRemotoHP.setFont(new Font("Arial", Font.BOLD, 12));

        jogadorRemotoPanel.add(lblJogadorRemotoHP);

        // Status do Inimigo
        JPanel inimigoPanel = new JPanel(new FlowLayout());
        inimigoPanel.setBackground(new Color(255, 220, 220));
        inimigoPanel.setBorder(BorderFactory.createTitledBorder("👹 " + inimigo.getNome()));

        lblInimigoHP = new JLabel("💀 HP: " + inimigo.getHp() + "/" + inimigo.getHpMax());
        lblInimigoHP.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblInimigoAtaque = new JLabel("⚔️ Ataque: " + inimigo.getAtaque());
        lblInimigoAtaque.setFont(new Font("Arial", Font.BOLD, 12));

        inimigoPanel.add(lblInimigoHP);
        inimigoPanel.add(lblInimigoAtaque);

        statusPanel.add(jogadorLocalPanel);
        statusPanel.add(jogadorRemotoPanel);
        statusPanel.add(inimigoPanel);

        // Painel de log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log do Combate Cooperativo"));

        txtLog = new JTextArea(15, 50);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLog.setEditable(false);
        txtLog.setBackground(Color.BLACK);
        txtLog.setForeground(Color.WHITE);

        JScrollPane scrollLog = new JScrollPane(txtLog);
        logPanel.add(scrollLog, BorderLayout.CENTER);

        // Painel de ações
        JPanel acoesPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações - " + (minhaVez ? "SUA VEZ" : "Aguardando Parceiro")));
        acoesPanel.setBackground(new Color(240, 240, 240));

        btnAtaqueNormal = criarBotaoCombate("⚔️ ATAQUE NORMAL", new Color(220, 60, 60));
        btnHabilidades = criarBotaoCombate("✨ HABILIDADES", new Color(60, 120, 220));
        btnItens = criarBotaoCombate("🎒 USAR ITEM", new Color(60, 180, 120));
        btnDefender = criarBotaoCombate("🛡️ DEFENDER", new Color(200, 150, 0));
        btnFugir = criarBotaoCombate("🏃 FUGIR", new Color(100, 100, 100));

        btnAtaqueNormal.addActionListener(e -> ataqueNormal());
        btnHabilidades.addActionListener(e -> usarHabilidade());
        btnItens.addActionListener(e -> usarItem());
        btnDefender.addActionListener(e -> defender());
        btnFugir.addActionListener(e -> tentarFugir());

        acoesPanel.add(btnAtaqueNormal);
        acoesPanel.add(btnHabilidades);
        acoesPanel.add(btnItens);
        acoesPanel.add(btnDefender);
        acoesPanel.add(btnFugir);

        add(tituloPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(acoesPanel, BorderLayout.SOUTH);

        // Configurar estado inicial dos botões
        if (minhaVez) {
            habilitarBotoes();
        } else {
            desabilitarBotoes();
        }
    }

    private JButton criarBotaoCombate(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(160, 50));

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

    private void iniciarCombateMultiplayer() {
        adicionarLog("⚔️ COMBATE COOPERATIVO INICIADO!");
        adicionarLog("🤝 Dupla: " + jogadorLocal.getNome() + " & " + 
                    (jogadorRemoto != null ? jogadorRemoto.getNome() : "Aguardando parceiro"));
        adicionarLog("👹 Inimigo: " + inimigo.getNome());
        adicionarLog("🎯 " + (minhaVez ? "Sua vez primeiro!" : "Aguardando " + 
                    (jogadorRemoto != null ? jogadorRemoto.getNome() : "parceiro") + "..."));

        // ✅ SINCRONIZAR INÍCIO DO COMBATE
        if (networkManager.isConnected()) {
            CombatActionData initialData = new CombatActionData(
                "COMBATE_INICIADO",
                inimigo.getHp(),
                jogadorLocal.getHp(),
                jogadorRemoto != null ? jogadorRemoto.getHp() : 0
            );
            
            GameMessage startMsg = new GameMessage(
                GameMessage.MessageType.COMBAT_START,
                initialData,
                jogadorLocal.getId()
            );
            networkManager.sendMessage(startMsg);
        }
    }

    // ✅ MÉTODOS DE AÇÃO SINCRONIZADOS
    protected void ataqueNormal() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        adicionarLog("⚔️ " + jogadorLocal.getNome() + " ataca!");
        
        // Cálculo simplificado do dano
        int dano = Math.max(1, jogadorLocal.getAtaque() - (inimigo.getDefesa() / 2));
        inimigo.setHp(inimigo.getHp() - dano);
        
        adicionarLog("💥 Causou " + dano + " de dano no " + inimigo.getNome() + "!");
        
        // ✅ SINCRONIZAR AÇÃO
        CombatActionData actionData = new CombatActionData(
            "ATAQUE_NORMAL", 
            inimigo.getHp(),
            jogadorLocal.getHp(),
            jogadorRemoto != null ? jogadorRemoto.getHp() : 0
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        verificarFimTurno();
    }
    
    protected void usarHabilidade() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        // Implementação simplificada - você pode adaptar do TelaCombate original
        adicionarLog("✨ " + jogadorLocal.getNome() + " usa habilidade!");
        
        int dano = Math.max(2, jogadorLocal.getAtaque()); // Dano maior que ataque normal
        inimigo.setHp(inimigo.getHp() - dano);
        
        adicionarLog("💫 Causou " + dano + " de dano mágico!");
        
        // ✅ SINCRONIZAR AÇÃO
        CombatActionData actionData = new CombatActionData(
            "HABILIDADE", 
            inimigo.getHp(),
            jogadorLocal.getHp(),
            jogadorRemoto != null ? jogadorRemoto.getHp() : 0
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        verificarFimTurno();
    }
    
    protected void usarItem() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        adicionarLog("🎒 " + jogadorLocal.getNome() + " usa item!");
        
        // Cura simplificada
        int cura = 15;
        jogadorLocal.setHp(Math.min(jogadorLocal.getHpMax(), jogadorLocal.getHp() + cura));
        adicionarLog("💖 Recuperou " + cura + " de HP!");
        
        // ✅ SINCRONIZAR AÇÃO
        CombatActionData actionData = new CombatActionData(
            "USAR_ITEM", 
            inimigo.getHp(),
            jogadorLocal.getHp(),
            jogadorRemoto != null ? jogadorRemoto.getHp() : 0
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        verificarFimTurno();
    }
    
    protected void defender() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        defesaAtiva = true;
        adicionarLog("🛡️ " + jogadorLocal.getNome() + " assume posição defensiva!");
        
        // ✅ SINCRONIZAR AÇÃO
        CombatActionData actionData = new CombatActionData(
            "DEFENDER", 
            inimigo.getHp(),
            jogadorLocal.getHp(),
            jogadorRemoto != null ? jogadorRemoto.getHp() : 0
        );
        
        GameMessage actionMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            actionData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(actionMsg);
        
        verificarFimTurno();
    }
    
    protected void tentarFugir() {
        if (!minhaVez) {
            JOptionPane.showMessageDialog(this, "Aguarde sua vez!");
            return;
        }
        
        double chanceFuga = 0.4; // Chance menor em multiplayer
        if (Math.random() < chanceFuga) {
            adicionarLog("🏃 " + jogadorLocal.getNome() + " fugiu do combate!");
            
            // ✅ SINCRONIZAR FUGA
            GameMessage fugaMsg = new GameMessage(
                GameMessage.MessageType.COMBAT_END,
                "FUGA",
                jogadorLocal.getId()
            );
            networkManager.sendMessage(fugaMsg);
            
            JOptionPane.showMessageDialog(this, "Fuga bem-sucedida!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            adicionarLog("❌ " + jogadorLocal.getNome() + " falhou ao tentar fugir!");
            verificarFimTurno();
        }
    }
    
    private void verificarFimTurno() {
        // Verificar se inimigo foi derrotado
        if (inimigo.getHp() <= 0) {
            vitoria();
            return;
        }
        
        // Passar a vez para o outro jogador
        passarVez();
        atualizarStatus();
        
        // Se for a vez do inimigo (após ambos jogadores), inimigo ataca
        if (!minhaVez && networkManager.isHost()) {
            // Host controla o turno do inimigo
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Pequeno delay para parecer mais natural
                    SwingUtilities.invokeLater(() -> {
                        turnoInimigo();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    private void passarVez() {
        minhaVez = false;
        desabilitarBotoes();
        
        adicionarLog("↪️ Vez passada para " + (jogadorRemoto != null ? jogadorRemoto.getNome() : "parceiro"));
        
        // ✅ NOTIFICAR OUTRO JOGADOR QUE É A VEZ DELE
        GameMessage vezMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            "SUA_VEZ",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(vezMsg);
    }
    
    protected void turnoInimigo() {
        if (inimigo.getHp() <= 0) return;
        
        adicionarLog("👹 Vez do " + inimigo.getNome() + "!");
        
        // Ataque simplificado do inimigo
        int dano = Math.max(1, inimigo.getAtaque() - (jogadorLocal.getAtaque() / 4));
        
        // Aplica defesa se ativa
        if (defesaAtiva) {
            int danoOriginal = dano;
            dano = Math.max(1, dano / 2);
            adicionarLog("🛡️ Defesa reduz o dano de " + danoOriginal + " para " + dano + "!");
            defesaAtiva = false;
        }
        
        jogadorLocal.setHp(jogadorLocal.getHp() - dano);
        adicionarLog("💀 " + inimigo.getNome() + " ataca " + jogadorLocal.getNome() + " causando " + dano + " de dano!");
        
        // ✅ SINCRONIZAR ATAQUE DO INIMIGO
        CombatActionData inimigoData = new CombatActionData(
            "INIMIGO_ATACOU", 
            inimigo.getHp(),
            jogadorLocal.getHp(),
            jogadorRemoto != null ? jogadorRemoto.getHp() : 0
        );
        
        GameMessage inimigoMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_ACTION,
            inimigoData,
            jogadorLocal.getId()
        );
        networkManager.sendMessage(inimigoMsg);
        
        if (jogadorLocal.getHp() <= 0) {
            derrota();
        } else {
            // Volta a vez para o primeiro jogador
            minhaVez = networkManager.isHost();
            if (minhaVez) {
                habilitarBotoes();
                adicionarLog("🎯 Sua vez novamente!");
            }
        }
        
        atualizarStatus();
    }
    
    protected void vitoria() {
        adicionarLog("🎉 " + inimigo.getNome() + " foi derrotado!");
        adicionarLog("🏆 Vitória cooperativa!");
        
        // ✅ SINCRONIZAR VITÓRIA
        GameMessage victoryMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_END,
            "VITORIA",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(victoryMsg);
        
        JOptionPane.showMessageDialog(this,
            "🎉 VITÓRIA COOPERATIVA!\n\n" +
            "Vocês derrotaram " + inimigo.getNome() + " juntos!\n" +
            "Uma dupla lendária!",
            "Vitória", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        
        // Notificar TelaAventuraMultiplayer sobre a vitória
        if (getOwner() instanceof TelaAventuraMultiplayer) {
            ((TelaAventuraMultiplayer) getOwner()).batalhaVencida();
        }
    }
    
    protected void derrota() {
        adicionarLog("💀 " + jogadorLocal.getNome() + " foi derrotado...");
        
        // ✅ SINCRONIZAR DERROTA
        GameMessage defeatMsg = new GameMessage(
            GameMessage.MessageType.COMBAT_END,
            "DERROTA",
            jogadorLocal.getId()
        );
        networkManager.sendMessage(defeatMsg);
        
        JOptionPane.showMessageDialog(this,
            "💀 DERROTA...\n\n" +
            "Você foi derrotado por " + inimigo.getNome() + ".\n" +
            "A jornada cooperativa termina aqui.",
            "Derrota", JOptionPane.ERROR_MESSAGE);
        
        dispose();
        
        // Notificar TelaAventuraMultiplayer sobre a derrota
        if (getOwner() instanceof TelaAventuraMultiplayer) {
            ((TelaAventuraMultiplayer) getOwner()).batalhaPerdida();
        }
    }
    
    // ✅ MÉTODOS PARA PROCESSAR MENSAGENS DE REDE
    public void processarMensagemCombate(GameMessage message) {
        if (message == null) return;
        
        switch (message.getType()) {
            case COMBAT_START:
                if (message.getData() instanceof CombatActionData) {
                    CombatActionData data = (CombatActionData) message.getData();
                    inimigo.setHp(data.getInimigoHp());
                    adicionarLog("📊 Combate sincronizado - Inimigo HP: " + data.getInimigoHp());
                }
                break;
                
            case COMBAT_ACTION:
                Object data = message.getData();
                if (data instanceof CombatActionData) {
                    CombatActionData actionData = (CombatActionData) data;
                    processarAcaoCombate(actionData, message.getPlayerId());
                } else if (data instanceof String) {
                    String acao = (String) data;
                    if ("SUA_VEZ".equals(acao)) {
                        minhaVez = true;
                        habilitarBotoes();
                        adicionarLog("🎯 Sua vez! Faça sua ação.");
                    }
                }
                break;
                
            case COMBAT_END:
                String resultado = (String) message.getData();
                if ("VITORIA".equals(resultado)) {
                    adicionarLog("🎉 Parceiro venceu o combate!");
                    vitoria();
                } else if ("DERROTA".equals(resultado)) {
                    adicionarLog("💀 Parceiro foi derrotado...");
                    derrota();
                } else if ("FUGA".equals(resultado)) {
                    adicionarLog("🏃 Parceiro fugiu do combate!");
                    JOptionPane.showMessageDialog(this, "Seu parceiro fugiu do combate!", "Fuga", JOptionPane.WARNING_MESSAGE);
                    dispose();
                }
                break;
        }
        atualizarStatus();
    }
    
    private void processarAcaoCombate(CombatActionData actionData, int playerId) {
        // Sincronizar estado do combate
        inimigo.setHp(actionData.getInimigoHp());
        
        // Atualizar HP dos jogadores (apenas visual)
        if (playerId != jogadorLocal.getId()) {
            // É ação do outro jogador
            if (jogadorRemoto != null) {
                jogadorRemoto.setHp(actionData.getJogadorHp());
            }
        }
        
        adicionarLog("📊 Ação sincronizada - Inimigo HP: " + actionData.getInimigoHp());
    }
    
    private void atualizarStatus() {
        lblJogadorHP.setText("❤️ HP: " + jogadorLocal.getHp() + "/" + jogadorLocal.getHpMax());
        lblJogadorMana.setText("🔵 Mana: " + jogadorLocal.getMana() + "/" + jogadorLocal.getManaMax());
        lblInimigoHP.setText("💀 HP: " + Math.max(0, inimigo.getHp()) + "/" + inimigo.getHpMax());
        
        if (jogadorRemoto != null) {
            lblJogadorRemotoHP.setText("❤️ HP: " + jogadorRemoto.getHp() + "/" + jogadorRemoto.getHpMax());
        }
        
        // Atualizar título do painel de ações
        ((javax.swing.border.TitledBorder) ((JPanel) getContentPane().getComponent(3)).getBorder())
            .setTitle("Ações - " + (minhaVez ? "SUA VEZ" : "Aguardando Parceiro"));
    }
    
    protected void adicionarLog(String mensagem) {
        txtLog.append(mensagem + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    
    protected void desabilitarBotoes() {
        btnAtaqueNormal.setEnabled(false);
        btnHabilidades.setEnabled(false);
        btnItens.setEnabled(false);
        btnDefender.setEnabled(false);
        btnFugir.setEnabled(false);
    }
    
    protected void habilitarBotoes() {
        btnAtaqueNormal.setEnabled(true);
        btnHabilidades.setEnabled(jogadorLocal.getHabilidades() != null && !jogadorLocal.getHabilidades().isEmpty());
        btnItens.setEnabled(jogadorLocal.getInventario() != null && !jogadorLocal.getInventario().isEmpty());
        btnDefender.setEnabled(true);
        btnFugir.setEnabled(true);
    }
    
    // ✅ GETTER PARA ACESSO EXTERNO
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}

// ✅ CLASSE AUXILIAR PARA DADOS DE AÇÃO DE COMBATE (se ainda não existir)
class CombatActionData implements java.io.Serializable {
    private String acao;
    private int inimigoHp;
    private int jogadorHp;
    private int jogadorRemotoHp;
    
    public CombatActionData(String acao, int inimigoHp, int jogadorHp, int jogadorRemotoHp) {
        this.acao = acao;
        this.inimigoHp = inimigoHp;
        this.jogadorHp = jogadorHp;
        this.jogadorRemotoHp = jogadorRemotoHp;
    }
    
    // Getters
    public String getAcao() { return acao; }
    public int getInimigoHp() { return inimigoHp; }
    public int getJogadorHp() { return jogadorHp; }
    public int getJogadorRemotoHp() { return jogadorRemotoHp; }
}