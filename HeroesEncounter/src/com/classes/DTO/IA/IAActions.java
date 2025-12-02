package com.classes.DTO.IA;

import com.classes.DTO.Inimigo;
import com.classes.Enums.TipoIA;
import java.util.*;

public class IAActions {
    public static List<String> getAcoesDisponiveis(Inimigo inimigo) {
        List<String> acoes = new ArrayList<>();
        acoes.add("ATAQUE_NORMAL");
        acoes.add("DEFENDER");
        
        String tipoInimigo = inimigo.getClass().getSimpleName();
        adicionarAcoesPorTipo(acoes, tipoInimigo);
        adicionarAcoesPorIA(acoes, inimigo.getTipoIA());
        
        if (!tipoInimigo.equals("Chefe") && 
            inimigo.getTipoIA() != TipoIA.CHEFE &&
            inimigo.getHp() < inimigo.getHpMax() * 0.25) {
            acoes.add("FUGIR");
        }
        
        return acoes;
    }
    
    private static void adicionarAcoesPorTipo(List<String> acoes, String tipo) {
        switch (tipo) {
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
                break;
            case "InimigoMagico":
                acoes.add("FEITICO_MAGICO");
                acoes.add("PROTECAO_MAGICA");
                acoes.add("CURAR");
                break;
        }
    }
    
    private static void adicionarAcoesPorIA(List<String> acoes, TipoIA tipoIA) {
        switch (tipoIA) {
            case AGRESSIVO:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
            case DEFENSIVA:
                acoes.add("CURAR");
                acoes.add("DEFENDER_FORTE");
                break;
            case ESTRATEGICA:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("BUFF_DEFESA");
                acoes.add("DEBUFF_JOGADOR");
                break;
            case CHEFE:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR_GUERRA");
                acoes.add("ATAQUE_ESPECIAL");
                break;
            case BALANCEADO:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
            case ALEATORIA:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                acoes.add("CURAR");
                acoes.add("BUFF_DEFESA");
                break;
        }
    }
}