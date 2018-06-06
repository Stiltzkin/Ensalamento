package br.edu.uniopet.webservice.service;

import java.util.List;

import br.edu.uniopet.webservice.model.dao.AgendaDAO;
import br.edu.uniopet.webservice.model.domain.Agenda;

public class AgendaService{
	private AgendaDAO dao = new AgendaDAO();
	
	public List<Agenda> getAgendas(){
		return dao.getAll();
	}
	
	public Agenda getById(Long idAgenda) {
		return dao.getById(idAgenda);
	}
	
	public Agenda save(Agenda agenda) {
		return dao.save(agenda);
	}
	
	public Agenda update(Agenda agenda) {
		return dao.update(agenda);
	}
	
	public Agenda delete(long idAgenda) {
		return dao.delete(idAgenda);
	}
	
	public List<Agenda> getByAgendaPagination(int firstResult, int maxResults){
		return dao.getByPagination(firstResult, maxResults);
	}

}
