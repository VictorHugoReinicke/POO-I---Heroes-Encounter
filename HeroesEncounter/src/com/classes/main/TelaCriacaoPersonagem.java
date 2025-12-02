package com.classes.main;

import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;

public class TelaCriacaoPersonagem extends JFrame {
    private TelaSelecao telaPai;
    private JTextField txtNome;
    private JComboBox<String> comboClasse;
    private JTextArea txtResultado;
    private JButton btnCriar;
    private JButton btnLimpar;
    private JButton btnVoltar;
    private JButton btnSair;

    public TelaCriacaoPersonagem(TelaSelecao pai) {
        this.telaPai = pai;
        initializeTela();
    }

    public TelaCriacaoPersonagem() {
        initializeTela();
    }

    private void initializeTela() {
        setTitle("Sistema de Criação de Personagem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("CRIAÇÃO DE PERSONAGEM - HEROES ENCOUNTER");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        JPanel entradaPanel = new JPanel();
        entradaPanel.setLayout(new GridBagLayout());
        entradaPanel.setBorder(BorderFactory.createTitledBorder("Dados do Personagem"));
        entradaPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNome = new JLabel("Nome do Herói:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));
        entradaPanel.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtNome = new JTextField(20);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 14));
        entradaPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblClasse = new JLabel("Classe:");
        lblClasse.setFont(new Font("Arial", Font.BOLD, 14));
        entradaPanel.add(lblClasse, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        String[] classes = {"Guerreiro", "Mago", "Paladino"};
        comboClasse = new JComboBox<>(classes);
        comboClasse.setFont(new Font("Arial", Font.PLAIN, 14));
        entradaPanel.add(comboClasse, gbc);

        JPanel resultadoPanel = new JPanel(new BorderLayout());
        resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado da Criação"));
        
        txtResultado = new JTextArea(15, 40);
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtResultado.setEditable(false);
        txtResultado.setBackground(new Color(240, 240, 240));
        txtResultado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        resultadoPanel.add(scrollResultado, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout());
        botoesPanel.setBackground(new Color(240, 240, 240));
        
        btnCriar = criarBotao("🎮 CRIAR PERSONAGEM", new Color(50, 150, 50));
        btnLimpar = criarBotao("🔄 LIMPAR", new Color(200, 150, 0));
        btnVoltar = criarBotao("↩️ VOLTAR", new Color(100, 100, 100));
        btnSair = criarBotao("🚪 SAIR", new Color(200, 50, 50));
        
        btnCriar.addActionListener(e -> criarPersonagem());
        btnLimpar.addActionListener(e -> limparCampos());
        btnVoltar.addActionListener(e -> voltar());
        btnSair.addActionListener(e -> System.exit(0));
        
        botoesPanel.add(btnCriar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnVoltar);
        botoesPanel.add(btnSair);

        add(tituloPanel, BorderLayout.NORTH);
        add(entradaPanel, BorderLayout.CENTER);
        add(resultadoPanel, BorderLayout.EAST);
        add(botoesPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(180, 40));

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

    private void criarPersonagem() {
        String nome = txtNome.getText().trim();
        String classe = (String) comboClasse.getSelectedItem();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, digite um nome para o seu personagem!",
                    "Nome Obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "O nome deve ter pelo menos 3 caracteres!",
                    "Nome Muito Curto",
                    JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        try {
            int idClasse;
            switch (classe) {
                case "Guerreiro":
                    idClasse = 1;
                    break;
                case "Mago":
                    idClasse = 2;
                    break;
                case "Paladino":
                    idClasse = 3;
                    break;
                default:
                    throw new IllegalArgumentException("Classe inválida: " + classe);
            }

            Jogador player1 = JogadorFactory.criarJogador(idClasse, nome);

            if (player1 != null) {
                StringBuilder resultado = new StringBuilder();
                resultado.append("==============================\n");
                resultado.append("JOGADOR CRIADO COM SUCESSO!\n");
                resultado.append("==============================\n\n");
                resultado.append("ID: ").append(player1.getId()).append("\n");
                resultado.append("Nome: ").append(player1.getNome()).append("\n");
                resultado.append("Classe: ").append(player1.getClass().getSimpleName()).append("\n");
                resultado.append("HP: ").append(player1.getHp()).append("\n");
                resultado.append("Mana: ").append(player1.getMana()).append("\n");
                resultado.append("Ouro: ").append(player1.getOuro()).append("\n");
                resultado.append("Arma equipada: ").append(
                    player1.getArmaEquipada() != null ? player1.getArmaEquipada().getNome() : "Nenhuma"
                ).append("\n\n");

                resultado.append("INVENTÁRIO:\n");
                if (player1.getInventario() != null && !player1.getInventario().isEmpty()) {
                    for (JogadorItem ji : player1.getInventario()) {
                        String equipadoInfo = ji.isEquipado() ? " [EQUIPADO]" : "";
                        resultado.append(" - ").append(ji.getItem().getNome())
                                .append(" (x").append(ji.getQuantidade()).append(")").append(equipadoInfo).append("\n");
                    }
                } else {
                    resultado.append(" - Vazio\n");
                }

                resultado.append("\nHABILIDADES:\n");
                if (player1.getHabilidades() != null && !player1.getHabilidades().isEmpty()) {
                    for (Habilidade h : player1.getHabilidades()) {
                        resultado.append(" - ").append(h.getNome()).append("\n");
                    }
                } else {
                    resultado.append(" - Nenhuma\n");
                }

                resultado.append("\n==============================\n");
                resultado.append("Personagem salvo no banco de dados!");

                txtResultado.setText(resultado.toString());
                txtResultado.setCaretPosition(0);

                JOptionPane.showMessageDialog(this,
                        "Personagem criado com sucesso!\n\n" +
                        "Nome: " + nome + "\n" +
                        "Classe: " + classe + "\n" +
                        "ID: " + player1.getId() + "\n\n" +
                        "Verifique o painel de resultado para detalhes completos.",
                        "Sucesso na Criação",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                throw new Exception("Falha ao criar personagem - objeto nulo retornado");
            }

        } catch (Exception e) {
            String mensagemErro = "ERRO AO CRIAR PERSONAGEM\n\n" +
                                 "Mensagem: " + e.getMessage() + 
                                 "\n\nDetalhes técnicos no console.";
            
            JOptionPane.showMessageDialog(this,
                    mensagemErro,
                    "Erro na Criação",
                    JOptionPane.ERROR_MESSAGE);
            
            txtResultado.setText("ERRO: " + e.getMessage() + "\n\nConsulte o console para detalhes técnicos.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        comboClasse.setSelectedIndex(0);
        txtResultado.setText("");
        txtNome.requestFocus();
    }

    private void voltar() {
        dispose();
        
        if (telaPai != null) {
            telaPai.setVisible(true);
        } else {
            new TelaSelecao().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaCriacaoPersonagem().setVisible(true);
        });
    }
}