package com.classes.DTO;

import com.classes.DTO.IA.*;
import com.classes.DTO.IA.estrategias.*;
import com.classes.Enums.TipoIA;
import java.util.*;

public class InimigoIA {
    private static Map<String, IIAStrategy> estrategias = new HashMap<>();
    
    static {
        estrategias.put("Besta", new IABesta());
        estrategias.put("Chefe", new IAChefe());
        estrategias.put("Ladrao", new IALadrao());
        estrategias.put("InimigoMagico", new IAMagico());
        estrategias.put("Generico", new IAGenerico());
    }
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        System.out.println("🤖 " + inimigo.getNome() + " está pensando...");
        
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        
        List<String> acoesDisponiveis = IAActions.getAcoesDisponiveis(inimigo);
        List<String> acoesFiltradas = IAHistorico.filtrarAcoesRecentes(
            inimigo.getId(), acoesDisponiveis);
        
        String tipoInimigo = inimigo.getClass().getSimpleName();
        IIAStrategy estrategia = estrategias.getOrDefault(tipoInimigo, estrategias.get("Generico"));
        
        String acaoEscolhida = estrategia.decidirAcao(
            inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesFiltradas);
        
        IAHistorico.registrarAcao(inimigo.getId(), acaoEscolhida);
        return acaoEscolhida;
    }
    
    public static void executarAcao(Inimigo inimigo, Jogador jogador, String acao) {
        IAExecutor.executarAcao(inimigo, jogador, acao);
    }
    
    public static String getDescricaoIA(TipoIA tipo) {
        try {
            Class<?> clazz = Class.forName("com.classes.DTO.IA.IADescricao");
            java.lang.reflect.Method method = clazz.getMethod("getDescricao", TipoIA.class);
            return (String) method.invoke(null, tipo);
        } catch (Exception e) {
            return getDescricaoFallback(tipo);
        }
    }
    
    private static String getDescricaoFallback(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo - Ataca sem piedade, raramente defende";
            case DEFENSIVA: return "🛡️ Defensivo - Prioriza sobrevivência, cura quando possível";
            case ESTRATEGICA: return "🎯 Estratégico - Usa buffs/debuffs, adapta-se à situação";
            case BALANCEADO: return "⚖️ Balanceado - Equilíbrio entre ataque e defesa";
            case ALEATORIA: return "🎲 Aleatório - Comportamento imprevisível";
            case CHEFE: return "👑 Chefe - Poderoso, usa habilidades especiais";
            default: return "Desconhecido";
        }
    }
    
    public static void limparHistorico(int idInimigo) {
        IAHistorico.limparHistorico(idInimigo);
    }
    
    public static void limparTodoHistorico() {
        IAHistorico.limparTodoHistorico();
    }
    
    public static void mostrarHistorico(int idInimigo) {
        IAHistorico.mostrarHistorico(idInimigo);
    }
    
    public static void limparDebuffs(Jogador jogador) {
        IAExecutor.limparDebuffs(jogador.getId());
    }
}