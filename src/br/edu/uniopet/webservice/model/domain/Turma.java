package br.edu.uniopet.webservice.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTurma;
	
	@Column(nullable = false)
	private String nm_turma;
	
	@Column(nullable = false)
	private int num_alunos;
	
	@ManyToOne
	@JoinColumn(name ="idCurso", nullable=false)	
	private Curso curso;

	public long getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(Long idTurma) {
		this.idTurma = idTurma;
	}

	public String getNm_turma() {
		return nm_turma;
	}

	public void setNm_turma(String nm_turma) {
		this.nm_turma = nm_turma;
	}

	public int getNum_alunos() {
		return num_alunos;
	}

	public void setNum_alunos(int num_alunos) {
		this.num_alunos = num_alunos;
	}
	
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public Turma() {
	}

	public Turma(Long id, String nm_turma, int num_alunos, Curso curso) {
		super();
		this.idTurma = id;
		this.nm_turma = nm_turma;
		this.num_alunos = num_alunos;
		this.curso = curso;
	}

	@Override
	public String toString() {
		return "Turma [id=" + idTurma + ", nm_turma=" + nm_turma + ", num_alunos=" + num_alunos + ", curso=" + curso
				+ "]";
	}

}
