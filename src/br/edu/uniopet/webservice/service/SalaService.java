package br.edu.uniopet.webservice.service;

import java.util.List;

import br.edu.uniopet.webservice.model.dao.SalaDAO;
import br.edu.uniopet.webservice.model.domain.Sala;

public class SalaService {
	private SalaDAO dao = new SalaDAO();
	
	public List<Sala> getAll() {
		return dao.getAll();
	}
	
	public Sala save(Sala sala) {
		return dao.save(sala);
	}
	
	public Sala update(Sala sala) {
		return dao.update(sala);
	}
	
	public Sala delete(Long idSala) {
		return dao.delete(idSala);
	}
	
	public List<Sala> getSalaByPagination(int firstResult, int maxResults){
		return dao.getByPagination(firstResult, maxResults);
	}
	
	public List<Sala> getSalaByName(String name){
		return dao.getByName(name);
	}
	
	public Sala getSalaById(Long idSala) {
		return dao.getById(idSala);
	}
}
