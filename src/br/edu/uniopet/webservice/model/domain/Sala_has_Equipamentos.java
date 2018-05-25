package br.edu.uniopet.webservice.model.domain;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="sala_has_equipamentos")
@AssociationOverrides({
	@AssociationOverride(name="pk.sala",
			joinColumns = @JoinColumn(name="idSala")),
	@AssociationOverride(name="pk.equipamento",
	joinColumns = @JoinColumn(name="idEquipamento"))
})
public class Sala_has_Equipamentos {

	private Sala_has_EquipamentosId pk;
	
	private int num_equipamento;

	@EmbeddedId
	public Sala_has_EquipamentosId getPk() {
		return pk;
	}

	public void setPk(Sala_has_EquipamentosId pk) {
		this.pk = pk;
	}

	public int getNum_equipamentos() {
		return num_equipamento;
	}

	public void setNum_equipamentos(int num_equipamentos) {
		this.num_equipamento = num_equipamentos;
	}
	
	@Transient
	public Sala getSala() {
		return getPk().getSala();
	}
	
	public void setSala(Sala sala) {
		pk.setSala(sala);
	}
	
	@Transient
	public Equipamento getEquipamento() {
		return getPk().getEquipamento();
	}
	
	public void setEquipamento(Equipamento equipamento) {
		pk.setEquipamento(equipamento);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num_equipamento;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sala_has_Equipamentos other = (Sala_has_Equipamentos) obj;
		if (num_equipamento != other.num_equipamento)
			return false;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}
	

}
