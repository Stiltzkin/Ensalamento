package br.edu.uniopet.webservice.service;

import java.util.List;

import br.edu.uniopet.webservice.model.dao.TurmaDAO;
import br.edu.uniopet.webservice.model.domain.Turma;

public class TurmaService {

	private TurmaDAO dao = new TurmaDAO();

	public List<Turma> getTurmas() {
		return dao.getAll();
	}

	public Turma getTurma(long idTurma) {
		return dao.getById(idTurma);
	}

	public Turma saveTurma(Turma turma) {
		return dao.save(turma);
	}

	public Turma updateTurma(Turma turma) {
		return dao.update(turma);
	}

	public Turma deleteTurma(long idTurma) {
		return dao.delete(idTurma);
	}

	public List<Turma> getTurmaByPagination(int firstResult, int maxResults) {
		return dao.getByPagination(firstResult, maxResults);
	}

	public List<Turma> getTurmaByName(String name) {
		return dao.getByName(name);
	}
}
