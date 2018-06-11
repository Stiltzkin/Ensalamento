package br.edu.uniopet.webservice.model.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Agenda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idAgenda;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@Temporal(javax.persistence.TemporalType.DATE)
	@Transient
	private Date data_inicio;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@Temporal(javax.persistence.TemporalType.DATE)
	@Transient
	private Date data_fim;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dia_reservada;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@Temporal(javax.persistence.TemporalType.TIME)
	private Date hora_inicio;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@Temporal(javax.persistence.TemporalType.TIME)
	private Date hora_fim;

	@ManyToOne
	@JoinColumn(name = "idTurma")
	private Turma turma;

	@ManyToOne
	@JoinColumn(name = "idSala")
	private Sala sala;

	@Transient
	private List<DiaDaSemanaCode> diaDaSemana;

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

	public Date getDia_reservada() {
		return dia_reservada;
	}

	public void setDia_reservada(Date dia_reservada) {
		this.dia_reservada = dia_reservada;
	}

	public Date getHora_inicio() {
		return hora_inicio;
	}

	public void setHora_inicio(Date hora_inicio) {
		this.hora_inicio = hora_inicio;
	}

	public Date getHora_fim() {
		return hora_fim;
	}

	public void setHora_fim(Date hora_fim) {
		this.hora_fim = hora_fim;
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

	public List<DiaDaSemanaCode> getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(List<DiaDaSemanaCode> diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public Agenda() {
	}

	public Agenda(long idAgenda, Date data_inicio, Date data_fim, Date dia_reservada, Date hora_inicio, Date hora_fim,
			Turma turma, Sala sala, List<DiaDaSemanaCode> diaDaSemana) {
		super();
		this.idAgenda = idAgenda;
		this.data_inicio = data_inicio;
		this.data_fim = data_fim;
		this.dia_reservada = dia_reservada;
		this.hora_inicio = hora_inicio;
		this.hora_fim = hora_fim;
		this.turma = turma;
		this.sala = sala;
		this.diaDaSemana = diaDaSemana;
	}

	@Override
	public String toString() {
		return "Agenda [idAgenda=" + idAgenda + ", data_inicio=" + data_inicio + ", data_fim=" + data_fim
				+ ", dia_reservada=" + dia_reservada + ", hora_inicio=" + hora_inicio + ", hora_fim=" + hora_fim
				+ ", turma=" + turma + ", sala=" + sala + ", diaDaSemana=" + diaDaSemana + "]";
	}
}
