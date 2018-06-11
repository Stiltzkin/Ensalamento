package br.edu.uniopet.webservice.service;

import java.util.List;

import javax.ws.rs.core.Response;

import br.edu.uniopet.webservice.model.dao.CursoDAO;
import br.edu.uniopet.webservice.model.domain.Curso;

public class CursoService {
	private CursoDAO dao = new CursoDAO();

	public List<Curso> getAll() {
		return dao.getAll();
	}

	public Curso getCurso(long idCurso) {
		return dao.getById(idCurso);
	}

	public Curso saveCurso(Curso curso) {
		return dao.save(curso);
	}

	public Curso updateCurso(Curso curso) {
		return dao.update(curso);
	}

	public Curso deleteCurso(long idCurso) {
		return dao.delete(idCurso);
	}

	public List<Curso> getCursoByPagination(int firstResult, int maxResults) {
		return dao.getByPagination(firstResult, maxResults);
	}

	public List<Curso> getCursoByName(String name) {
		return dao.getByName(name);
	}
}
