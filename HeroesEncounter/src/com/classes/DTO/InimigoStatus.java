package com.classes.DTO;

public class InimigoStatus {

	private int id;
	private int idInimigo;
	private int idStatus;
	private int turnoRestante;

	private Inimigo inimigo;
	private Status status;

	public InimigoStatus() {
	}

	public InimigoStatus(int idInimigo, int idStatus, int turnoRestante) {
		this.idInimigo = idInimigo;
		this.idStatus = idStatus;
		this.turnoRestante = turnoRestante;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdInimigo() {
		return idInimigo;
	}

	public void setIdInimigo(int idInimigo) {
		this.idInimigo = idInimigo;
	}

	public int getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}

	public int getTurnoRestante() {
		return turnoRestante;
	}

	public void setTurnoRestante(int turnoRestante) {
		this.turnoRestante = turnoRestante;
	}

	
	public Inimigo getInimigo() {
		return inimigo;
	}

	public void setInimigo(Inimigo inimigo) {
		this.inimigo = inimigo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void decrementarTurno() {
		if (this.turnoRestante > 0) {
			this.turnoRestante--;
		}
	}

}
