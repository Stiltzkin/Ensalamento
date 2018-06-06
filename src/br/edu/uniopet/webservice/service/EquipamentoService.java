package br.edu.uniopet.webservice.service;

import java.util.List;

import br.edu.uniopet.webservice.model.dao.EquipamentoDAO;
import br.edu.uniopet.webservice.model.domain.Equipamento;

public class EquipamentoService {
	EquipamentoDAO dao = new EquipamentoDAO();
	
	public List<Equipamento> getAll(){
		return dao.getAll();
	}
	
	public Equipamento getEquipamento(Long idEquipamento) {
		return dao.getById(idEquipamento);
	}
	
	public Equipamento saveEquipamento(Equipamento equipamento) {
		return dao.save(equipamento);
	}
	
	public Equipamento updateEquipamento(Equipamento equipamento) {
		return dao.update(equipamento);
	}
	
	public Equipamento deleteEquipamento(Long idEquipamento) {
		return dao.delete(idEquipamento);
	}
	
	public List<Equipamento> getEquipamentoByPagination(int firstResult, int maxResult){
		return dao.getByPagination(firstResult, maxResult);
	}
	
	public List<Equipamento> getEquipamentoByName(String name){
		return dao.getByName(name);
	}

}
