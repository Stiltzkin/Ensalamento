package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Sala;

public class SalaDAO {
	public List<Sala> getAll() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Sala> salas = null;
		
		try {
			salas = em.createQuery("select s from Sala s", Sala.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Problemas ao buscar salas do banco de dados. " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return salas;
	}
	
	public List<Sala> getByPagination(int firstResult, int maxResults){
		List<Sala> salas = null;
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			salas = em.createQuery("select s from Sala s", Sala.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar lista de salas do banco de dados. "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return salas;
	}
	
	public List<Sala> getByName(String name){
		List<Sala> salas = null;
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			salas = em.createQuery("select s from Salas s where s.nome like :name", Sala.class)
					.setParameter("name", "%"+ name +"%").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar lista de salas do banco de dados. "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		
		if(salas.isEmpty()) {
			throw new DAOException("A consulta de salas não retornou elementos. ", ErrorCode.NOT_FOUND.getCode());
		}
		
		return salas;
	}
	
	public Sala getById(Long idSala) {
		EntityManager em = JPAUtil.getEntityManager();
		Sala sala = null;
		
		try {
			sala = em.find(Sala.class, idSala);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar sala por id. "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}
		return sala;
	}
	
	public Sala save(Sala sala) {
		EntityManager em = JPAUtil.getEntityManager();
		
		if(!salaIsValid(sala)) {
			throw new DAOException("Sala com dados incompletps. ", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			em.getTransaction().begin();
			em.persist(sala);
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Problemas ao salvar sala no banco de dados. "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return sala;
	}
	
	public Sala update(Sala sala) {
		EntityManager em = JPAUtil.getEntityManager();
		Sala salaManaged = null;
		
		try {
			em.getTransaction().begin();
			salaManaged = em.find(Sala.class, sala.getIdSala());
			salaManaged.setNm_sala(sala.getNm_sala());
			salaManaged.setTipo_sala(sala.getTipo_sala());
			salaManaged.setNum_cadeiras(sala.getNum_cadeiras());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Sala informada não existe. " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar sala. ", ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return salaManaged;
		
	}
	
	public Sala delete(long idSala) {
		EntityManager em = JPAUtil.getEntityManager();
		Sala sala = null;
		
		try {
			em.getTransaction().begin();
			sala = em.find(Sala.class, idSala);
			em.remove(sala);
			em.getTransaction().commit();
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Sala informado não existe. " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao deletar sala. ", ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return sala;
		
		
	}
	
	private boolean salaIsValid(Sala sala){
		if (sala.getNm_sala().isEmpty()) {
			return false;
		}
//		if(sala.getTipo_sala() == ) {
//			return false;
//		}
		return true;
	}
}
