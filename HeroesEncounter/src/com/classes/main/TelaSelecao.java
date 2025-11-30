package com.classes.main;

import javax.swing.*;
import java.awt.*;

public class TelaSelecao extends JFrame {
    private JButton btnNovoPersonagem;
    private JButton btnCarregarPersonagem;
    private JButton btnSair;

    public TelaSelecao() {
        initializeTela();
    }

    private void initializeTela() {
        setTitle("Heroes Encounter - Seleção de Personagem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("HEROES ENCOUNTER");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // Painel central com botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(3, 1, 30, 30));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));
        botoesPanel.setBackground(new Color(240, 240, 240));

        // Botão Novo Personagem
        btnNovoPersonagem = criarBotaoGrande("🎮 NOVO PERSONAGEM", new Color(60, 120, 220));
        btnNovoPersonagem.addActionListener(e -> abrirCriacaoPersonagem());

        // Botão Carregar Personagem
        btnCarregarPersonagem = criarBotaoGrande("📂 CARREGAR PERSONAGEM", new Color(60, 180, 120));
        btnCarregarPersonagem.addActionListener(e -> abrirListaPersonagens());

        // Botão Sair
        btnSair = criarBotaoGrande("🚪 SAIR", new Color(200, 60, 60));
        btnSair.addActionListener(e -> System.exit(0));

        botoesPanel.add(btnNovoPersonagem);
        botoesPanel.add(btnCarregarPersonagem);
        botoesPanel.add(btnSair);

        // Painel inferior
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(30, 30, 70));
        JLabel footer = new JLabel("Sistema de Gerenciamento de Personagens");
        footer.setForeground(Color.WHITE);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footer);

        add(tituloPanel, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private JButton criarBotaoGrande(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(300, 80));

        // Efeito hover
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

    private void abrirCriacaoPersonagem() {
        TelaCriacaoPersonagem telaCriacao = new TelaCriacaoPersonagem(this);
        telaCriacao.setVisible(true);

    }

    private void abrirListaPersonagens() {
        TelaListaPersonagens telaLista = new TelaListaPersonagens(this);
        telaLista.setVisible(true);

    }

    // Método para voltar para esta tela
    public void voltarParaSelecao() {
        this.setVisible(true);  
        this.toFront();         
        this.requestFocus();   
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaSelecao().setVisible(true);
        });
    }
}