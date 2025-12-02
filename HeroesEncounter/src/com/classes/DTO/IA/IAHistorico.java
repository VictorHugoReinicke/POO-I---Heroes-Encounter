package com.classes.DTO.IA;

import java.util.*;

public class IAHistorico {
    private static final Map<Integer, List<String>> historicoAcoes = new HashMap<>();
    private static final int MAX_HISTORICO = 5;
    
    public static List<String> filtrarAcoesRecentes(int idInimigo, List<String> acoesDisponiveis) {
        if (!historicoAcoes.containsKey(idInimigo)) {
            return acoesDisponiveis;
        }
        
        List<String> historico = historicoAcoes.get(idInimigo);
        List<String> acoesFiltradas = new ArrayList<>(acoesDisponiveis);
        
        for (String acaoRecente : historico) {
            if (!acaoRecente.equals("ATAQUE_NORMAL") && !acaoRecente.equals("DEFENDER")) {
                acoesFiltradas.remove(acaoRecente);
            }
        }
        
        return acoesFiltradas.isEmpty() ? acoesDisponiveis : acoesFiltradas;
    }
    
    public static void registrarAcao(int idInimigo, String acao) {
        historicoAcoes.putIfAbsent(idInimigo, new ArrayList<>());
        List<String> historico = historicoAcoes.get(idInimigo);
        
        historico.add(0, acao);
        if (historico.size() > MAX_HISTORICO) {
            historico.subList(MAX_HISTORICO, historico.size()).clear();
        }
    }
    
    public static void limparHistorico(int idInimigo) {
        historicoAcoes.remove(idInimigo);
    }
    
    public static void limparTodoHistorico() {
        historicoAcoes.clear();
    }
    
    public static void mostrarHistorico(int idInimigo) {
        if (historicoAcoes.containsKey(idInimigo)) {
            List<String> historico = historicoAcoes.get(idInimigo);
            System.out.println("📊 Histórico de ações (últimas " + historico.size() + "):");
            for (int i = 0; i < historico.size(); i++) {
                System.out.println("  " + (i+1) + ". " + historico.get(i));
            }
        }
    }
}