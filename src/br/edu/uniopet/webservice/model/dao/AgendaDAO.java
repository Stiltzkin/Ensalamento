package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Agenda;
import br.edu.uniopet.webservice.util.AgendamentoUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendaDAO {

	public List<Agenda> getAll() {
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

		if (idAgenda <= 0) {
			throw new DAOException("O id precisa ser maior que 0. ", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			agenda = em.find(Agenda.class, idAgenda);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar agendamento por id no banco. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agenda;
	}

	public List<Agenda> getByPagination(int firstResult, int maxResults) {
		List<Agenda> agendas;
		EntityManager em = JPAUtil.getEntityManager();

		try {
			agendas = em.createQuery("select a from Agenda a", Agenda.class).setFirstResult(firstResult - 1)
					.setMaxResults(maxResults).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar agendamentos do banco de dados. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
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

		// Se for data exata (não intervalo entre datas)
		if (agenda.getData_fim() == null) {
			Agenda newAgenda = dataExata(agenda);
			try {
				em.getTransaction().begin();
				em.persist(newAgenda);
				em.getTransaction().commit();
			} catch (RuntimeException ex) {
				em.getTransaction().rollback();
				throw new DAOException("Erro ao salvar agendamento no banco de dados: " + ex.getMessage(),
						ErrorCode.SERVER_ERROR.getCode());
			} finally {
				em.close();
			}
			return newAgenda;
		}
		AgendamentoUtil agendamentoUtil = new AgendamentoUtil();
		ArrayList<Date> diasAgendarArray = agendamentoUtil.diasSelecionados(agenda);

		// Cria array do que vai pro banco de dados
		ArrayList<Agenda> agendamento = new ArrayList<>();
		for (int i = 0; i < diasAgendarArray.size(); i++) {
			Agenda newAgenda = new Agenda();
			newAgenda.setSala(agenda.getSala());
			newAgenda.setTurma(agenda.getTurma());
			newAgenda.setHora_inicio(agenda.getHora_inicio());
			newAgenda.setHora_fim(agenda.getHora_fim());
			newAgenda.setDia_reservada(diasAgendarArray.get(i));
			agendamento.add(newAgenda);
		}

		if (haConflito(agendamento, em) == false) {
			throw new DAOException("Conflito na agenda.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			for (int k = 0; k < agendamento.size(); k++) {
				em.persist(agendamento.get(k));
			}
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar agendamento no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
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

		// Se for data exata (não intervalo entre datas)
		if (agenda.getData_fim() == null) {
			Agenda newAgenda = dataExata(agenda);
			try {
				em.getTransaction().begin();
				em.persist(newAgenda);
				em.getTransaction().commit();
			} catch (RuntimeException ex) {
				em.getTransaction().rollback();
				throw new DAOException("Erro ao salvar agendamento no banco de dados: " + ex.getMessage(),
						ErrorCode.SERVER_ERROR.getCode());
			} finally {
				em.close();
			}
			return newAgenda;
		}

		AgendamentoUtil agendamentoUtil = new AgendamentoUtil();
		ArrayList<Date> diasAgendarArray = agendamentoUtil.diasSelecionados(agenda);

		// Cria array do que vai pro banco de dados
		ArrayList<Agenda> agendamento = new ArrayList<>();
		for (int i = 0; i < diasAgendarArray.size(); i++) {
			Agenda newAgenda = new Agenda();
			newAgenda.setSala(agenda.getSala());
			newAgenda.setTurma(agenda.getTurma());
			newAgenda.setHora_inicio(agenda.getHora_inicio());
			newAgenda.setHora_fim(agenda.getData_fim());
			newAgenda.setDia_reservada(diasAgendarArray.get(i));
			agendamento.add(newAgenda);
		}

		if (haConflito(agendamento, em)) {
			throw new DAOException("Conflito na agenda.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();

			for (int k = 0; k < agendamento.size(); k++) {
				agendaManaged = em.find(Agenda.class, agenda.getIdAgenda());
				agendaManaged.setDia_reservada(agendamento.get(k).getDia_reservada());
				agendaManaged.setHora_inicio(agendamento.get(k).getHora_inicio());
				agendaManaged.setHora_fim(agendamento.get(k).getHora_fim());
				agendaManaged.setSala(agendamento.get(k).getSala());
				agendaManaged.setTurma(agendamento.get(k).getTurma());
				em.persist(agendaManaged);
			}
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Agenda informado para atualização não existe: " + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
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
			throw new DAOException("Erro ao remover agendamento do banco. " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return agenda;

	}

	private boolean agendaIsValid(Agenda agenda) {
		if (agenda.getData_inicio() == null || agenda.getHora_inicio() == null || agenda.getHora_fim() == null
				|| agenda.getSala() == null || agenda.getTurma() == null) {
			return false;
		}
		return true;
	}

	private boolean haConflito(ArrayList<Agenda> agendamento, EntityManager em) {
		List<Agenda> agendasList = null;
		AgendamentoUtil agendamentoUtil = new AgendamentoUtil();
		agendasList = em.createQuery("select a from Agenda a", Agenda.class).getResultList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for (int z = 0; z < agendamento.size(); z++) {
			for (int i = 0; i < agendasList.size(); i++) {
				String agendamentoStr = sdf.format(agendamento.get(z).getDia_reservada());
				String agendasListStr = sdf.format(agendasList.get(i).getDia_reservada());

				Calendar agendasListHoraInicio = agendamentoUtil.toCalendar(agendasList.get(i).getHora_inicio());
				Calendar agendasListHoraFim = agendamentoUtil.toCalendar(agendasList.get(i).getHora_fim());
				Calendar agendamentoHoraInicio = agendamentoUtil.toCalendar(agendamento.get(z).getHora_fim());
				Calendar agendamentoHoraFim = agendamentoUtil.toCalendar(agendamento.get(z).getHora_fim());

				if (agendamentoStr.equals(agendasListStr)) {
					if (agendamentoHoraInicio.before(agendasListHoraFim)
							&& agendamentoHoraInicio.after(agendasListHoraInicio)) {
						return false;
					}
					if (agendamentoHoraFim.before(agendasListHoraFim)
							&& agendamentoHoraFim.after(agendasListHoraInicio)) {
						return false;
					}
				}

			}
		}
		return true;
	}

	private Agenda dataExata(Agenda agenda) {
		Agenda newAgenda = new Agenda();
		newAgenda.setDia_reservada(agenda.getData_inicio());
		newAgenda.setHora_inicio(agenda.getHora_inicio());
		newAgenda.setHora_fim(agenda.getData_fim());
		newAgenda.setTurma(agenda.getTurma());
		newAgenda.setSala(agenda.getSala());

		return newAgenda;
	}
}
