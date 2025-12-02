package com.classes.DTO;

import com.classes.DTO.*;
import com.classes.Enums.TipoIA;
import com.classes.main.GeminiAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.*;

public class InimigoIA {
    //oloco, ficou top pra caralho
    private static final boolean USAR_GEMINI_PARA_CHEFES = true;
    private static final boolean USAR_GEMINI_PARA_TODOS = false;
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        System.out.println(" - " + inimigo.getNome() + " está pensando...");
        
        if (inimigo.getTipoIA() == TipoIA.CHEFE && USAR_GEMINI_PARA_CHEFES) {
            try {
                return GeminiAI.decidirAcao(inimigo, jogador);
            } catch (Exception e) {
                System.err.println(" - Erro no Gemini, usando IA local: " + e.getMessage());
                return usarIALocal(inimigo, jogador);
            }
        }
        
        if (USAR_GEMINI_PARA_TODOS) {
            try {
                return GeminiAI.decidirAcao(inimigo, jogador);
            } catch (Exception e) {
                System.err.println(" - Erro no Gemini, usando IA local: " + e.getMessage());
                return usarIALocal(inimigo, jogador);
            }
        }
        
        return usarIALocal(inimigo, jogador);
    }
    
    private static String usarIALocal(Inimigo inimigo, Jogador jogador) {
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        
        List<String> acoesDisponiveis = getAcoesDisponiveis(inimigo);
        Random rand = new Random();
        
        String tipoInimigo = inimigo.getClass().getSimpleName();

        return switch (tipoInimigo) {
            case "Besta" -> decidirAcaoBesta(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
            case "Chefe" -> decidirAcaoChefe(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
            case "Ladrao" -> decidirAcaoLadrao(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
            case "InimigoMagico" ->
                    decidirAcaoMagico(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
            default -> decidirAcaoGenerico(inimigo, jogador, hpPercentInimigo, hpPercentJogador, acoesDisponiveis);
        };
    }
    
    private static String decidirAcaoBesta(Inimigo inimigo, Jogador jogador, 
                                           double hpInimigo, double hpJogador, 
                                           List<String> acoes) {
        Random rand = new Random();
        
        if (hpInimigo < 0.3) {
            if (acoes.contains("ATAQUE_FEROZ")) {
                return "ATAQUE_FEROZ";
            }
            return "ATAQUE_NORMAL";
        }
        
        if (hpJogador < 0.4) {
            return "ATAQUE_NORMAL";
        }
        
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
        
        if (hpInimigo < 0.4 && acoes.contains("REGENERAR")) {
            return "REGENERAR";
        }
        
        if (hpJogador < 0.5 && acoes.contains("ATAQUE_ESPECIAL")) {
            return "ATAQUE_ESPECIAL";
        }
        
        if (rand.nextDouble() < 0.3) {
            if (acoes.contains("ATAQUE_ESPECIAL")) {
                return "ATAQUE_ESPECIAL";
            }
        }
        
        if (rand.nextDouble() < 0.2 && acoes.contains("GRITAR_GUERRA")) {
            return "GRITAR_GUERRA";
        }
        
        if (rand.nextDouble() < 0.6 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        }
        
        return "ATAQUE_NORMAL";
    }
    
    private static String decidirAcaoLadrao(Inimigo inimigo, Jogador jogador,
                                           double hpInimigo, double hpJogador,
                                           List<String> acoes) {
        Random rand = new Random();
        
        if (hpInimigo < 0.25) {
            if (acoes.contains("FUGIR")) {
                return "FUGIR";
            }
        }
        
        if (hpJogador > 0.7 && acoes.contains("ESQUIVAR")) {
            if (rand.nextDouble() < 0.4) {
                return "ESQUIVAR";
            }
        }
        
        if (hpInimigo > 0.6 && hpJogador < 0.5 && acoes.contains("ATAQUE_RAPIDO")) {
            return "ATAQUE_RAPIDO";
        }
        
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
        
        if (hpInimigo < 0.4 && acoes.contains("PROTECAO_MAGICA")) {
            return "PROTECAO_MAGICA";
        }
        
        if (hpJogador > 0.6 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        }
        
        if (hpJogador < 0.3) {
            return "ATAQUE_NORMAL";
        }
        
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
                
            default:
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
        
        acoes.add("ATAQUE_NORMAL");
        acoes.add("DEFENDER");
        
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
        
        if (!tipoInimigo.equals("Chefe") &&
            inimigo.getTipoIA() != TipoIA.CHEFE &&
            inimigo.getHp() < inimigo.getHpMax() * 0.25) {
            if (!acoes.contains("FUGIR")) acoes.add("FUGIR");
        }
        
        return acoes;
    }
    
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
                System.out.println(" - " + inimigo.getNome() + " ataca ferozmente!");
                break;
                
            case "ATAQUE_RAPIDO":
                atacar(inimigo, jogador, 0.7);
                if (new Random().nextDouble() < 0.3) {
                    System.out.println("⚡ Ataque rápido!");
                    atacar(inimigo, jogador, 0.5);
                }
                break;
                
            case "ATAQUE_ESPECIAL":
                atacar(inimigo, jogador, 2.0);
                System.out.println(" - " + inimigo.getNome() + " usa um ataque especial!");
                break;
                
            case "DEFENDER":
                System.out.println(" - " + inimigo.getNome() + " se defende!");
                inimigo.setDefesa(inimigo.getDefesa() + 5);
                break;
                
            case "DEFENDER_FORTE":
                System.out.println(" - " + inimigo.getNome() + " assume uma postura defensiva!");
                inimigo.setDefesa(inimigo.getDefesa() + 10);
                break;
                
            case "ESQUIVAR":
                System.out.println(" - " + inimigo.getNome() + " esquivou!");
                break;
                
            case "GRITAR":
                System.out.println(" - " + inimigo.getNome() + " grita para intimidar!");
                break;
                
            case "GRITAR_GUERRA":
                System.out.println(" - " + inimigo.getNome() + " solta um grito de guerra!");
                inimigo.setAtaque(inimigo.getAtaque() + 3);
                break;
                
            case "RUGIDO":
                System.out.println(" - " + inimigo.getNome() + " solta um rugido assustador!");
                break;
                
            case "CURAR":
                int cura = 10 + new Random().nextInt(10);
                inimigo.setHp(Math.min(inimigo.getHp() + cura, inimigo.getHpMax()));
                System.out.println(" - " + inimigo.getNome() + " se cura em " + cura + " HP!");
                break;
                
            case "REGENERAR":
                int regeneracao = 20 + new Random().nextInt(15);
                inimigo.setHp(Math.min(inimigo.getHp() + regeneracao, inimigo.getHpMax()));
                System.out.println(" - " + inimigo.getNome() + " se regenera em " + regeneracao + " HP!");
                break;
                
            case "FEITICO_MAGICO":
                atacar(inimigo, jogador, 1.3);
                System.out.println(" - " + inimigo.getNome() + " lança um feitiço mágico!");
                break;
                
            case "PROTECAO_MAGICA":
                System.out.println(" - " + inimigo.getNome() + " conjura uma proteção mágica!");
                inimigo.setDefesa(inimigo.getDefesa() + 8);
                break;
                
            case "FUGIR":
                System.out.println(" - " + inimigo.getNome() + " tenta fugir do combate!");
                double chanceFuga = 0.3 + (1.0 - (double) inimigo.getHp() / inimigo.getHpMax()) * 0.7;
                if (new Random().nextDouble() < chanceFuga) {
                    System.out.println(" - Fuga bem sucedida!");
                } else {
                    System.out.println(" - Fuga falhou!");
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
        
        System.out.println(" - " + inimigo.getNome() + " ataca " + jogador.getNome() +
                " causando " + danoFinal + " de dano!");
    }
    
    public static String getDescricaoIA(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "Agressivo";
            case DEFENSIVA: return "Defensivo";
            case ESTRATEGICA: return "Estratégico";
            case BALANCEADO: return "Balanceado";
            case ALEATORIA: return "Aleatório";
            case CHEFE: return "Chefe (Gemini IA)";
            default: return "Desconhecido";
        }
    }
}