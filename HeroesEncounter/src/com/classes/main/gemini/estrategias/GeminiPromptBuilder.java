package com.classes.main.gemini.estrategias;

import com.classes.DTO.*;
import com.classes.DTO.IA.IAActions;
import com.classes.Enums.TipoIA;
import java.util.List;

public class GeminiPromptBuilder {
    
    public static String criarPromptCompleto(Inimigo inimigo, Jogador jogador) {
        List<String> acoes = IAActions.getAcoesDisponiveis(inimigo);
        
        String tipoInimigo = inimigo.getClass().getSimpleName();
        String descricaoPersonalidade = getDescricaoPersonalidade(inimigo.getTipoIA());
        
        return String.format(
            "ROLE: Você é '%s', um %s em um jogo RPG.\n\n" +
            
            "STATUS ATUAL:\n" +
            "SEU ESTADO:\n" +
            "- Nome: %s\n" +
            "- Tipo: %s\n" +
            "- HP: %d/%d (%.1f%%)\n" +
            "- Ataque: %d\n" +
            "- Defesa: %d\n" +
            "- Personalidade: %s\n\n" +
            
            "JOGADOR:\n" +
            "- Nome: %s\n" +
            "- HP: %d/%d (%.1f%%)\n\n" +
            
            "CONTEXTO:\n" +
            "%s\n\n" +
            
            "AÇÕES DISPONÍVEIS:\n" +
            "%s\n\n" +
            
            "DIRETRIZES:\n" +
            "1. Comporte-se como um %s\n" +
            "2. %s\n" +
            "3. Escolha a ação mais estratégica baseada na situação\n" +
            "4. Chefes NUNCA devem fugir\n\n" +
            
            "FORMATO DE RESPOSTA:\n" +
            "Responda APENAS com o nome exato da ação em MAIÚSCULAS.\n" +
            "Exemplo: ATAQUE_NORMAL\n\n" +
            
            "Sua decisão:",
            
            // Dados
            inimigo.getNome(), tipoInimigo,
            inimigo.getNome(), tipoInimigo,
            inimigo.getHp(), inimigo.getHpMax(), 
            (double) inimigo.getHp() / inimigo.getHpMax() * 100,
            inimigo.getAtaque(), inimigo.getDefesa(),
            inimigo.getTipoIA().toString(),
            jogador.getNome(),
            jogador.getHp(), jogador.getHpMax(),
            (double) jogador.getHp() / jogador.getHpMax() * 100,
            analisarSituacao(inimigo, jogador),
            formatarAcoes(acoes),
            tipoInimigo.toLowerCase(),
            descricaoPersonalidade
        );
    }
    
    private static String getDescricaoPersonalidade(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "Seja agressivo e impiedoso. Priorize causar dano máximo.";
            case DEFENSIVA: return "Seja defensivo e cauteloso. Priorize sobrevivência e cura.";
            case ESTRATEGICA: return "Seja estratégico e inteligente. Use buffs/debuffs quando vantajoso.";
            case CHEFE: return "Seja um CHEFE impiedoso! Use habilidades especiais e intimide o jogador.";
            case ALEATORIA: return "Seja imprevisível! Suas decisões podem variar muito.";
            case BALANCEADO: return "Mantenha equilíbrio entre ataque e defesa conforme a situação.";
            default: return "Comporte-se de forma inteligente baseada na situação.";
        }
    }
    
    private static String analisarSituacao(Inimigo inimigo, Jogador jogador) {
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        
        if (hpPercentInimigo < 0.2 && hpPercentJogador < 0.2) {
            return "⚡ SITUAÇÃO CRÍTICA: Ambos estão quase morrendo! Decisão crucial.";
        } else if (hpPercentInimigo < 0.3) {
            return "⚠️ PERIGO: Você está gravemente ferido! Priorize sobrevivência ou cura.";
        } else if (hpPercentJogador < 0.3) {
            return "🎯 OPORTUNIDADE: Jogador está fraco! Atacar para finalizar o combate.";
        } else if (hpPercentInimigo < 0.5) {
            return "🟡 CUIDADO: Você está moderadamente ferido. Seja estratégico.";
        } else if (hpPercentJogador < 0.5) {
            return "🟡 Jogador está moderamente ferido. Bom momento para ataques fortes.";
        } else {
            return "🟢 SITUAÇÃO NORMAL: Ambos com vida razoável. Mantenha pressão.";
        }
    }
    
    private static String formatarAcoes(List<String> acoes) {
        StringBuilder sb = new StringBuilder();
        for (String acao : acoes) {
            sb.append("- ").append(acao).append("\n");
        }
        return sb.toString();
    }
}