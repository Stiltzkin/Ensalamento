package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Curso;

public class CursoDAO {
	public List<Curso> getAll() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Curso> cursos = null;

		try {
			cursos = em.createQuery("select p from Curso p", Curso.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao recuperar todos os cursos do banco: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		return cursos;
	}

	public List<Curso> getByPagination(int firstResult, int maxResults) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Curso> cursos = null;

		try {
			cursos = em.createQuery("select p from Curso p", Curso.class).setFirstResult(firstResult - 1)
					.setMaxResults(maxResults).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar cursos no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return cursos;
	}

	public List<Curso> getByName(String name) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Curso> cursos = null;

		try {
			cursos = em.createQuery("select p from Curso p where p.nome_curso like :name", Curso.class)
					.setParameter("name", "%" + name + "%").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar cursos por nome no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return cursos;
	}

	public Curso getById(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		Curso curso = null;

		if (id <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			curso = em.find(Curso.class, id);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar curso por id no banco de dados.", ErrorCode.SERVER_ERROR.getCode());
		}

		return curso;
	}

	public Curso save(Curso curso) {
		EntityManager em = JPAUtil.getEntityManager();

		if (!cursoIsValid(curso)) {
			throw new DAOException("Curso com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			em.persist(curso);
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar curso no banco de dados: ", ErrorCode.BAD_REQUEST.getCode());
		} finally {
			em.close();
		}
		return curso;
	}

	public Curso update(Curso curso) {
		EntityManager em = JPAUtil.getEntityManager();
		Curso cursoManaged = null;

		if (curso.getId() <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
		if (!cursoIsValid(curso)) {
			throw new DAOException("Curso com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			cursoManaged = em.find(Curso.class, curso.getId());
			cursoManaged.setNome_curso(curso.getNome_curso());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Curso informado para atualiação não existe." + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar curso no banco de dados.", ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return cursoManaged;
	}

	public Curso delete(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		Curso curso = null;

		if (id <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			curso = em.find(Curso.class, id);
			em.remove(curso);
			em.getTransaction().commit();
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Curso informado para remover não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());

		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover curso.", ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return curso;
	}

	private boolean cursoIsValid(Curso curso) {
		try {
			if (curso.getNome_curso().isEmpty()) {
				return false;
			}
		} catch (NullPointerException ex) {
			throw new DAOException("Curso com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		return true;
	}
}
