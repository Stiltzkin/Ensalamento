package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Equipamento;

public class EquipamentoDAO {
	public List<Equipamento> getAll() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Equipamento> equipamentos;

		try {
			equipamentos = em.createQuery("select e from Equipamento e", Equipamento.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao recuperar lista de equipamentos do banco de dados. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return equipamentos;
	}

	public Equipamento getById(Long idEquipamento) {
		EntityManager em = JPAUtil.getEntityManager();
		Equipamento equipamento = null;

		if (idEquipamento <= 0) {
			throw new DAOException("O id precisa ser maior que 0. ", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			equipamento = em.find(Equipamento.class, idEquipamento);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar equipamento por id no banco. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		}
		return equipamento;
	}

	public List<Equipamento> getByPagination(int firstResult, int maxResults) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Equipamento> equipamentos;

		try {
			equipamentos = em.createQuery("select e from Equioamento e", Equipamento.class)
					.setFirstResult(firstResult - 1).setMaxResults(maxResults).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar equipamento no banco de dados. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return equipamentos;
	}

	public List<Equipamento> getByName(String name) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Equipamento> equipamentos = null;

		try {
			equipamentos = em
					.createQuery("select e from Equipamento e where e.desc_equipamento like :name", Equipamento.class)
					.setParameter("name", "%" + name + "%").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar equipamentos do banco de dados. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (equipamentos.isEmpty()) {
			throw new DAOException("A consulta não retornou elementos. ", ErrorCode.NOT_FOUND.getCode());
		}
		return equipamentos;
	}

	public Equipamento save(Equipamento equipamento) {
		EntityManager em = JPAUtil.getEntityManager();

		if (!equipamentoIsValid(equipamento)) {
			throw new DAOException("Equipamentos com dados incompletos. ", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			em.persist(equipamento);
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao salvar equipamento no banco de dados. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return equipamento;
	}

	public Equipamento update(Equipamento equipamento) {
		EntityManager em = JPAUtil.getEntityManager();
		Equipamento equipamentoManaged = null;

		if (!equipamentoIsValid(equipamento)) {
			throw new DAOException("Equipamentos com dados inconpletos. ", ErrorCode.BAD_REQUEST.getCode());
		}
		if (equipamento.getIdEquipamento() <= 0) {
			throw new DAOException("O id precisa ser maior que 0. ", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			equipamentoManaged = em.find(Equipamento.class, equipamento.getIdEquipamento());
			equipamentoManaged.setDesc_equipamento(equipamento.getDesc_equipamento());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Equipamento informado para edição não existe. ", ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar equipamento. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return equipamentoManaged;
	}
	
	public Equipamento delete(Long idEquipamento) {
		EntityManager em = JPAUtil.getEntityManager();
		Equipamento equipamento = null;
		
		try {
			em.getTransaction().begin();
			equipamento = em.find(Equipamento.class, idEquipamento);
			em.remove(equipamento);
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Turma informado para remoção não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover equipamento do banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return equipamento;
	}

	public boolean equipamentoIsValid(Equipamento equipamanto) {
		if (equipamanto.getDesc_equipamento().isEmpty()) {
			return false;
		}
		return true;
	}
}
