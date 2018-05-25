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
public class Equipamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEquipamento;

	@Column(nullable = false)
	private String desc_equipamento;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.equipamento")
	private Set<Sala_has_Equipamentos> sala_has_Equipamentos = new HashSet<Sala_has_Equipamentos>();

	public Long getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(Long idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getDesc_equipamento() {
		return desc_equipamento;
	}

	public void setDesc_equipamento(String desc_equipamento) {
		this.desc_equipamento = desc_equipamento;
	}

	public Set<Sala_has_Equipamentos> getSala_has_Equipamentos() {
		return sala_has_Equipamentos;
	}

	public void setSala_has_Equipamentos(Set<Sala_has_Equipamentos> sala_has_Equipamentos) {
		this.sala_has_Equipamentos = sala_has_Equipamentos;
	}

	public Equipamento() {
	}

	public Equipamento(Long idEquipamento, String desc_equipamento, Set<Sala_has_Equipamentos> sala_has_Equipamentos) {
		super();
		this.idEquipamento = idEquipamento;
		this.desc_equipamento = desc_equipamento;
		this.sala_has_Equipamentos = sala_has_Equipamentos;
	}

	@Override
	public String toString() {
		return "Equipamento [idEquipamento=" + idEquipamento + ", desc_equipamento=" + desc_equipamento
				+ ", sala_has_Equipamentos=" + sala_has_Equipamentos + "]";
	}

}
