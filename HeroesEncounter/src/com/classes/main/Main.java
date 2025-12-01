package com.classes.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Inicia a interface gráfica com a Tela de Seleção
        SwingUtilities.invokeLater(() -> {
            TelaSelecao tela = new TelaSelecao();
            tela.setVisible(true);
        });
    }
}