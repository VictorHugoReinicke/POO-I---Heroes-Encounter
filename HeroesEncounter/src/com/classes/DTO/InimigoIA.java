package com.classes.DTO;

import com.classes.DTO.*;
import com.classes.Enums.TipoIA;
import com.classes.main.GeminiAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.*;

public class InimigoIA {
    
    private static final boolean USAR_GEMINI_PARA_CHEFES = true;
    private static final boolean USAR_GEMINI_PARA_TODOS = false;
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        System.out.println("🤖 " + inimigo.getNome() + " está pensando...");
        
        // Se for CHEFE e estiver habilitado para usar Gemini
        if (inimigo.getTipoIA() == TipoIA.CHEFE && USAR_GEMINI_PARA_CHEFES) {
            try {
                return GeminiAI.decidirAcao(inimigo, jogador);
            } catch (Exception e) {
                System.err.println("❌ Erro no Gemini, usando IA local: " + e.getMessage());
                return usarIALocal(inimigo, jogador);
            }
        }
        
        // Se quiser usar Gemini para todos os tipos (opcional)
        if (USAR_GEMINI_PARA_TODOS) {
            try {
                return GeminiAI.decidirAcao(inimigo, jogador);
            } catch (Exception e) {
                System.err.println("❌ Erro no Gemini, usando IA local: " + e.getMessage());
                return usarIALocal(inimigo, jogador);
            }
        }
        
        // Para outros tipos, usar IA local
        return usarIALocal(inimigo, jogador);
    }
    
    private static String usarIALocal(Inimigo inimigo, Jogador jogador) {
        // Lógica baseada no tipo de inimigo
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        
        List<String> acoesDisponiveis = getAcoesDisponiveis(inimigo);
        Random rand = new Random();
        
        // Verificar o tipo concreto do inimigo
        String tipoInimigo = inimigo.getClass().getSimpleName();
        
        // Lógica específica por tipo de inimigo
        switch (tipoInimigo) {
            case "Besta":
                return decidirAcaoBesta(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
                
            case "Chefe":
                return decidirAcaoChefe(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
                
            case "Ladrao":
                return decidirAcaoLadrao(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
                
            case "InimigoMagico":
                return decidirAcaoMagico(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
                
            default:
                return decidirAcaoGenerico(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
        }
    }
    
    private static String decidirAcaoBesta(Inimigo inimigo, Jogador jogador, 
                                           double hpInimigo, double hpJogador, 
                                           List<String> acoes) {
        Random rand = new Random();
        
        // Bestas são mais agressivas quando feridas
        if (hpInimigo < 0.3) {
            if (acoes.contains("ATAQUE_FEROZ")) {
                return "ATAQUE_FEROZ";
            }
            return "ATAQUE_NORMAL";
        }
        
        // Se jogador está fraco, atacar
        if (hpJogador < 0.4) {
            return "ATAQUE_NORMAL";
        }
        
        // 80% chance de atacar, 20% de defender
        if (rand.nextDouble() < 0.8) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
    
    private static String decidirAcaoChefe(Inimigo inimigo, Jogador jogador,
                                          double hpInimigo, double hpJogador,
                                          List<String> acoes) {
        Random rand = new Random();
        
        // Chefes usam habilidades especiais
        if (hpInimigo < 0.4 && acoes.contains("REGENERAR")) {
            return "REGENERAR";
        }
        
        if (hpJogador < 0.5 && acoes.contains("ATAQUE_ESPECIAL")) {
            return "ATAQUE_ESPECIAL";
        }
        
        // 30% chance de usar habilidade especial
        if (rand.nextDouble() < 0.3) {
            if (acoes.contains("ATAQUE_ESPECIAL")) {
                return "ATAQUE_ESPECIAL";
            }
        }
        
        // 20% chance de usar grito de guerra
        if (rand.nextDouble() < 0.2 && acoes.contains("GRITAR_GUERRA")) {
            return "GRITAR_GUERRA";
        }
        
        // 60% ataque poderoso, 40% ataque normal
        if (rand.nextDouble() < 0.6 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        }
        
        return "ATAQUE_NORMAL";
    }
    
    private static String decidirAcaoLadrao(Inimigo inimigo, Jogador jogador,
                                           double hpInimigo, double hpJogador,
                                           List<String> acoes) {
        Random rand = new Random();
        
        // Ladrões são oportunistas e fogem facilmente
        if (hpInimigo < 0.25) {
            if (acoes.contains("FUGIR")) {
                return "FUGIR";
            }
        }
        
        // Se jogador está forte, usar esquiva
        if (hpJogador > 0.7 && acoes.contains("ESQUIVAR")) {
            if (rand.nextDouble() < 0.4) {
                return "ESQUIVAR";
            }
        }
        
        // Ataque rápido se tem vantagem
        if (hpInimigo > 0.6 && hpJogador < 0.5 && acoes.contains("ATAQUE_RAPIDO")) {
            return "ATAQUE_RAPIDO";
        }
        
        // 70% ataque normal, 30% defender
        if (rand.nextDouble() < 0.7) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
    
    private static String decidirAcaoMagico(Inimigo inimigo, Jogador jogador,
                                           double hpInimigo, double hpJogador,
                                           List<String> acoes) {
        Random rand = new Random();
        
        // Magos são defensivos e estratégicos
        if (hpInimigo < 0.4 && acoes.contains("PROTECAO_MAGICA")) {
            return "PROTECAO_MAGICA";
        }
        
        // Se jogador está forte, usar feitiço mágico
        if (hpJogador > 0.6 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        }
        
        // Se jogador está fraco, atacar diretamente
        if (hpJogador < 0.3) {
            return "ATAQUE_NORMAL";
        }
        
        // 50% chance de usar magia, 30% defender, 20% atacar
        double chance = rand.nextDouble();
        if (chance < 0.5 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        } else if (chance < 0.8) {
            return "DEFENDER";
        } else {
            return "ATAQUE_NORMAL";
        }
    }
    
    private static String decidirAcaoGenerico(Inimigo inimigo, Jogador jogador,
                                             double hpInimigo, double hpJogador,
                                             List<String> acoes) {
        Random rand = new Random();
        
        // Lógica baseada na personalidade da IA
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                if (hpJogador < 0.5 && acoes.contains("ATAQUE_PODEROSO")) {
                    return "ATAQUE_PODEROSO";
                }
                return "ATAQUE_NORMAL";
                
            case DEFENSIVA:
                if (hpInimigo < 0.5) {
                    return "DEFENDER";
                }
                if (rand.nextDouble() < 0.7) {
                    return "ATAQUE_NORMAL";
                } else {
                    return "DEFENDER";
                }
                
            case ESTRATEGICA:
                if (hpJogador < 0.4 && hpInimigo > 0.6) {
                    return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
                }
                if (hpInimigo < 0.5) {
                    return "DEFENDER";
                }
                return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
                
            case ALEATORIA:
                return acoes.get(rand.nextInt(acoes.size()));
                
            case CHEFE:
                return decidirAcaoChefe(inimigo, jogador, hpInimigo, hpJogador, acoes);
                
            default: // BALANCEADO
                if (hpInimigo < 0.4) {
                    return "DEFENDER";
                }
                if (hpJogador < 0.4) {
                    return "ATAQUE_NORMAL";
                }
                return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
        }
    }
    
    private static List<String> getAcoesDisponiveis(Inimigo inimigo) {
        List<String> acoes = new ArrayList<>();
        
        // Ações básicas para todos
        acoes.add("ATAQUE_NORMAL");
        acoes.add("DEFENDER");
        
        // Ações baseadas no tipo de inimigo
        String tipoInimigo = inimigo.getClass().getSimpleName();
        
        switch (tipoInimigo) {
            case "Besta":
                acoes.add("ATAQUE_FEROZ");
                acoes.add("RUGIDO");
                break;
                
            case "Chefe":
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("ATAQUE_ESPECIAL");
                acoes.add("GRITAR_GUERRA");
                acoes.add("REGENERAR");
                break;
                
            case "Ladrao":
                acoes.add("ATAQUE_RAPIDO");
                acoes.add("ESQUIVAR");
                acoes.add("ROUBAR");
                if (inimigo.getHp() < inimigo.getHpMax() * 0.3) {
                    acoes.add("FUGIR");
                }
                break;
                
            case "InimigoMagico":
                acoes.add("FEITICO_MAGICO");
                acoes.add("PROTECAO_MAGICA");
                acoes.add("CURAR");
                break;
        }
        
        // Ações baseadas no tipo de IA
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                if (!acoes.contains("ATAQUE_PODEROSO")) acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
                
            case DEFENSIVA:
                if (!acoes.contains("CURAR")) acoes.add("CURAR");
                acoes.add("DEFENDER_FORTE");
                break;
                
            case ESTRATEGICA:
                if (!acoes.contains("ATAQUE_PODEROSO")) acoes.add("ATAQUE_PODEROSO");
                acoes.add("BUFF_DEFESA");
                acoes.add("DEBUFF_JOGADOR");
                break;
        }
        
        // Inimigos não-chefes podem fugir se estiverem muito feridos
        if (!tipoInimigo.equals("Chefe") && 
            inimigo.getTipoIA() != TipoIA.CHEFE &&
            inimigo.getHp() < inimigo.getHpMax() * 0.25) {
            if (!acoes.contains("FUGIR")) acoes.add("FUGIR");
        }
        
        return acoes;
    }
    
    // Método para executar a ação escolhida
    public static void executarAcao(Inimigo inimigo, Jogador jogador, String acao) {
        switch (acao) {
            case "ATAQUE_NORMAL":
                atacar(inimigo, jogador, 1.0);
                break;
                
            case "ATAQUE_PODEROSO":
                atacar(inimigo, jogador, 1.5);
                break;
                
            case "ATAQUE_FEROZ":
                atacar(inimigo, jogador, 1.8);
                System.out.println("🐾 " + inimigo.getNome() + " ataca ferozmente!");
                break;
                
            case "ATAQUE_RAPIDO":
                atacar(inimigo, jogador, 0.7);
                // Ataque rápido pode dar um ataque extra
                if (new Random().nextDouble() < 0.3) {
                    System.out.println("⚡ Ataque rápido!");
                    atacar(inimigo, jogador, 0.5);
                }
                break;
                
            case "ATAQUE_ESPECIAL":
                atacar(inimigo, jogador, 2.0);
                System.out.println("💥 " + inimigo.getNome() + " usa um ataque especial!");
                break;
                
            case "DEFENDER":
                System.out.println("🛡️ " + inimigo.getNome() + " se defende!");
                // Aumenta defesa temporariamente
                inimigo.setDefesa(inimigo.getDefesa() + 5);
                break;
                
            case "DEFENDER_FORTE":
                System.out.println("🛡️ " + inimigo.getNome() + " assume uma postura defensiva!");
                inimigo.setDefesa(inimigo.getDefesa() + 10);
                break;
                
            case "ESQUIVAR":
                System.out.println("🌀 " + inimigo.getNome() + " esquivou!");
                // Próximo ataque do jogador tem 50% de chance de errar
                break;
                
            case "GRITAR":
                System.out.println("🗣️ " + inimigo.getNome() + " grita para intimidar!");
                // Reduz ataque do jogador temporariamente
                break;
                
            case "GRITAR_GUERRA":
                System.out.println("⚔️ " + inimigo.getNome() + " solta um grito de guerra!");
                // Aumenta ataque do inimigo
                inimigo.setAtaque(inimigo.getAtaque() + 3);
                break;
                
            case "RUGIDO":
                System.out.println("🦁 " + inimigo.getNome() + " solta um rugido assustador!");
                // Causa medo no jogador
                break;
                
            case "CURAR":
                int cura = 10 + new Random().nextInt(10);
                inimigo.setHp(Math.min(inimigo.getHp() + cura, inimigo.getHpMax()));
                System.out.println("💚 " + inimigo.getNome() + " se cura em " + cura + " HP!");
                break;
                
            case "REGENERAR":
                int regeneracao = 20 + new Random().nextInt(15);
                inimigo.setHp(Math.min(inimigo.getHp() + regeneracao, inimigo.getHpMax()));
                System.out.println("✨ " + inimigo.getNome() + " se regenera em " + regeneracao + " HP!");
                break;
                
            case "FEITICO_MAGICO":
                atacar(inimigo, jogador, 1.3);
                System.out.println("🔮 " + inimigo.getNome() + " lança um feitiço mágico!");
                break;
                
            case "PROTECAO_MAGICA":
                System.out.println("🛡️✨ " + inimigo.getNome() + " conjura uma proteção mágica!");
                inimigo.setDefesa(inimigo.getDefesa() + 8);
                break;
                
            case "FUGIR":
                System.out.println("🏃 " + inimigo.getNome() + " tenta fugir do combate!");
                // Chance de fuga baseada na vida
                double chanceFuga = 0.3 + (1.0 - (double) inimigo.getHp() / inimigo.getHpMax()) * 0.7;
                if (new Random().nextDouble() < chanceFuga) {
                    System.out.println("✅ Fuga bem sucedida!");
                    // Aqui você implementaria a lógica para terminar o combate
                } else {
                    System.out.println("❌ Fuga falhou!");
                }
                break;
                
            default:
                atacar(inimigo, jogador, 1.0);
                break;
        }
    }
    
    private static void atacar(Inimigo inimigo, Jogador jogador, double multiplicador) {
        int danoBase = (int)(inimigo.getAtaque() * multiplicador);
        int danoFinal = Math.max(1, danoBase - jogador.getDefesa());
        
        jogador.receberDano(danoFinal);
        
        System.out.println("⚔️ " + inimigo.getNome() + " ataca " + jogador.getNome() +
                " causando " + danoFinal + " de dano!");
    }
    
    // Método auxiliar para obter descrição da IA
    public static String getDescricaoIA(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo";
            case DEFENSIVA: return "🛡️ Defensivo";
            case ESTRATEGICA: return "🎯 Estratégico";
            case BALANCEADO: return "⚖️ Balanceado";
            case ALEATORIA: return "🎲 Aleatório";
            case CHEFE: return "👑 Chefe (Gemini IA)";
            default: return "Desconhecido";
        }
    }
}