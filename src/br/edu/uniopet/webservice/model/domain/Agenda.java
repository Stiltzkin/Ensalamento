package br.edu.uniopet.webservice.model.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Agenda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idAgenda;

	@Column(nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date data_inicio;

	@Column(nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date data_fim;
	
	@ManyToOne
	@JoinColumn(name = "idTurma")
	private Turma turma;
	
	@ManyToOne
	@JoinColumn(name="idSala")
	private Sala sala;

	public long getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(long idAgenda) {
		this.idAgenda = idAgenda;
	}

	public Date getData_inicio() {
		return data_inicio;
	}

	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}

	public Date getData_fim() {
		return data_fim;
	}

	public void setData_fim(Date data_fim) {
		this.data_fim = data_fim;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Agenda() {
	}
	
	public Agenda(long idAgenda, Date data_inicio, Date data_fim, Turma turma, Sala sala) {
		super();
		this.idAgenda = idAgenda;
		this.data_inicio = data_inicio;
		this.data_fim = data_fim;
		this.turma = turma;
		this.sala = sala;
	}

	@Override
	public String toString() {
		return "Agenda [idAgenda=" + idAgenda + ", data_inicio=" + data_inicio + ", data_fim=" + data_fim + ", turma="
				+ turma + ", sala=" + sala + "]";
	}

	



}
