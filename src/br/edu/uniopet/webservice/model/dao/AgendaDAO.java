package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Agenda;

public class AgendaDAO {
	public List<Agenda> getAll(){
		EntityManager em = JPAUtil.getEntityManager();
		List<Agenda> agendas = null;
		
		try {
			agendas = em.createQuery("select a from Agenda a", Agenda.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao recuperar todas as agendas do banco: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agendas;
	}
	
	public Agenda getById(Long idAgenda) {
		EntityManager em = JPAUtil.getEntityManager();
		Agenda agenda = null;
		
		if(idAgenda <= 0) {
			throw new DAOException("O id precisa ser maior que 0. ", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			agenda = em.find(Agenda.class, idAgenda);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar agendamento por id no banco. " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agenda;
	}
	
	public List<Agenda> getByPagination(int firstResult, int maxResults){
		List<Agenda> agendas;
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			agendas = em.createQuery("select a from Agenda a", Agenda.class).setFirstResult(firstResult - 1)
					.setMaxResults(maxResults).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar agendamentos do banco de dados. " +ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		
		if (agendas.isEmpty()) {
			throw new DAOException("Lista de agendamento vazio. ", ErrorCode.NOT_FOUND.getCode());
		}
		
		return agendas;
	}
	
	public Agenda save(Agenda agenda) {
		EntityManager em = JPAUtil.getEntityManager();
		
		if (!agendaIsValid(agenda)) {
			throw new DAOException("Agenda com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			em.getTransaction().begin();
			em.persist(agenda);
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar agenda no banco de dados: " + ex.getMessage(),ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agenda;
	}
	
	public Agenda update(Agenda agenda) {
		EntityManager em = JPAUtil.getEntityManager();
		Agenda agendaManaged = null;
		
		if (!agendaIsValid(agenda)) {
			throw new DAOException("Agenda com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			em.getTransaction().begin();
			agendaManaged = em.find(Agenda.class, agenda.getIdAgenda());
			agendaManaged.setData_inicio(agenda.getData_inicio());
			agendaManaged.setData_fim(agenda.getData_fim());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Agenda informado para atualização não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex){
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar agendamento no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agendaManaged;
	}
	
	public Agenda delete(Long idAgenda) {
		EntityManager em = JPAUtil.getEntityManager();
		Agenda agenda = null;
		
		if (idAgenda <= 0) {
			throw new DAOException("O id do agendamento precisa ser maior que 0. ", ErrorCode.BAD_REQUEST.getCode());
		}
		try {
			em.getTransaction().begin();
			agenda = em.find(Agenda.class, idAgenda);
			em.remove(agenda);
			em.getTransaction().commit();
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Agendamento informado para remoção não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover agendamento do banco. "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agenda;
		
	}
	
	private boolean agendaIsValid(Agenda agenda) {
		if(agenda.getData_inicio() == null) {
			return false;
		}
		if(agenda.getData_fim() == null) {
			return false;
		}
		return true;
	}
}
