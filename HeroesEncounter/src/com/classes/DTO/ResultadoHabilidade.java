package com.classes.DTO;

public class ResultadoHabilidade {
	private String habilidadeUsada;
	private int danoCausado;
	private int curaAplicada;
	private boolean critico;
	private String statusAplicado;
	private String efeitoAplicado;
	private String mensagem;
	private boolean sucesso;

	// Getters e Setters
	public String getHabilidadeUsada() {
		return habilidadeUsada;
	}

	public void setHabilidadeUsada(String habilidadeUsada) {
		this.habilidadeUsada = habilidadeUsada;
	}

	public int getDanoCausado() {
		return danoCausado;
	}

	public void setDanoCausado(int danoCausado) {
		this.danoCausado = danoCausado;
	}

	public int getCuraAplicada() {
		return curaAplicada;
	}

	public void setCuraAplicada(int curaAplicada) {
		this.curaAplicada = curaAplicada;
	}

	public boolean isCritico() {
		return critico;
	}

	public void setCritico(boolean critico) {
		this.critico = critico;
	}

	public String getStatusAplicado() {
		return statusAplicado;
	}

	public void setStatusAplicado(String statusAplicado) {
		this.statusAplicado = statusAplicado;
	}

	public String getEfeitoAplicado() {
		return efeitoAplicado;
	}

	public void setEfeitoAplicado(String efeitoAplicado) {
		this.efeitoAplicado = efeitoAplicado;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
}