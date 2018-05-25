package br.edu.uniopet.webservice.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCurso;

	@Column(nullable = false)
	private String nome_curso;

	public long getId() {
		return idCurso;
	}

	public void setId(Long idCurso) {
		this.idCurso = idCurso;
	}

	public String getNome_curso() {
		return nome_curso;
	}

	public void setNome_curso(String nome_curso) {
		this.nome_curso = nome_curso;
	}

	public Curso() {
	}

	public Curso(Long idCurso, String nome_curso) {
		super();
		this.idCurso = idCurso;
		this.nome_curso = nome_curso;
	}

	@Override
	public String toString() {
		return "Curso [id=" + idCurso + ", nome_curso=" + nome_curso + "]";
	}



}
