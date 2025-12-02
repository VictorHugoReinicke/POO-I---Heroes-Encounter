package com.classes.DTO.IA;

import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.List;

public interface IIAStrategy {
	String decidirAcao(Inimigo inimigo, Jogador jogador, double hpPercentInimigo, double hpPercentJogador,
			List<String> acoesFiltradas);
}