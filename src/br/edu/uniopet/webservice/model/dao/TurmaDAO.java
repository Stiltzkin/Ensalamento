package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Turma;

public class TurmaDAO {
	public List<Turma> getAll() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Turma> turmas = null;

		try {
			turmas = em.createQuery("select p from Turma p", Turma.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao recuperar todas as turmas do banco: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		return turmas;
	}

	public List<Turma> getByPagination(int firstResult, int maxResults) {
		List<Turma> turmas;
		EntityManager em = JPAUtil.getEntityManager();

		try {
			turmas = em.createQuery("select p from Turma p", Turma.class).setFirstResult(firstResult - 1)
					.setMaxResults(maxResults).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar turmas no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (turmas.isEmpty()) {
			throw new DAOException("Página com turmas vazia.", ErrorCode.NOT_FOUND.getCode());
		}

		return turmas;
	}

	public List<Turma> getByName(String name) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Turma> turmas = null;

		try {
			turmas = em.createQuery("select p from Turma p where p.nm_turma like :name", Turma.class)
					.setParameter("name", "%" + name + "%").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar turmas por nome no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (turmas.isEmpty()) {
			throw new DAOException("A consulta não retornou elementos.", ErrorCode.NOT_FOUND.getCode());
		}

		return turmas;
	}

	public Turma getById(Long idTurma) {
		EntityManager em = JPAUtil.getEntityManager();
		Turma turma = null;

		if (idTurma <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			turma = em.find(Turma.class, idTurma);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar turma por id no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (turma == null) {
			throw new DAOException("Turma de id " + idTurma + " não existe.", ErrorCode.NOT_FOUND.getCode());
		}
		return turma;
	}

	public Turma save(Turma turma) {
		EntityManager em = JPAUtil.getEntityManager();

		if (!produtoIsValid(turma)) {
			throw new DAOException("Turma com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			em.persist(turma);
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar turma no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return turma;
	}

	public Turma update(Turma turma) {
		EntityManager em = JPAUtil.getEntityManager();
		Turma turmaManaged = null;

		if (turma.getIdTurma() <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
		if (!produtoIsValid(turma)) {
			throw new DAOException("Turma com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			turmaManaged = em.find(Turma.class, turma.getIdTurma());
			turmaManaged.setNm_turma(turma.getNm_turma());
			turmaManaged.setNum_alunos(turma.getNum_alunos());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Turma informado para atualização não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar turma no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return turmaManaged;
	}

	public Turma delete(Long idTurma) {
		EntityManager em = JPAUtil.getEntityManager();
		Turma turma = null;

		if (idTurma <= 0) {
			throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			turma = em.find(Turma.class, idTurma);
			em.remove(turma);
			em.getTransaction().commit();
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Turma informado para remoção não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover turma do banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		return turma;
	}

	private boolean produtoIsValid(Turma turma) {
		try {
			if ((turma.getNm_turma().isEmpty()) || (turma.getNum_alunos() < 0))
				return false;
		} catch (NullPointerException ex) {
			throw new DAOException("Turma com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		return true;
	}
}
