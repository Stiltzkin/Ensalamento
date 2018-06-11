package br.edu.uniopet.webservice.model.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSala;

	@Column(nullable = false)
	private String nm_sala;

	@Column(nullable = false)
	private int tipo_sala;

	@Column
	private int num_cadeiras;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.sala")
//	private Set<Sala_has_Equipamentos> sala_has_Equipamentos = new HashSet<Sala_has_Equipamentos>();

	public Long getIdSala() {
		return idSala;
	}

	public void setIdSala(Long idSala) {
		this.idSala = idSala;
	}

	public String getNm_sala() {
		return nm_sala;
	}

	public void setNm_sala(String nm_sala) {
		this.nm_sala = nm_sala;
	}

	public int getTipo_sala() {
		return tipo_sala;
	}

	public void setTipo_sala(int tipo_sala) {
		this.tipo_sala = tipo_sala;
	}

	public int getNum_cadeiras() {
		return num_cadeiras;
	}

	public void setNum_cadeiras(int num_cadeiras) {
		this.num_cadeiras = num_cadeiras;
	}

//	public Set<Sala_has_Equipamentos> getSala_has_Equipamentos() {
//		return sala_has_Equipamentos;
//	}
//
//	public void setSala_has_Equipamentos(Set<Sala_has_Equipamentos> sala_has_Equipamentos) {
//		this.sala_has_Equipamentos = sala_has_Equipamentos;
//	}

	public Sala(Long idSala, String nm_sala, int tipo_sala, int num_cadeiras,
			Set<Sala_has_Equipamentos> sala_has_Equipamentos) {
		super();
		this.idSala = idSala;
		this.nm_sala = nm_sala;
		this.tipo_sala = tipo_sala;
		this.num_cadeiras = num_cadeiras;
//		this.sala_has_Equipamentos = sala_has_Equipamentos;
	}

	public Sala() {
	}

}
