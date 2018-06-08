package br.edu.uniopet.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.uniopet.webservice.exceptions.DAOException;
import br.edu.uniopet.webservice.exceptions.ErrorCode;
import br.edu.uniopet.webservice.model.domain.Agenda;
import br.edu.uniopet.webservice.model.domain.DiaDaSemanaCode;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

		/// TODO::
		// Xunxera
		Date startDate = agenda.getData_inicio();
		Date endDate = agenda.getData_fim();
		LocalDate startDateLd = asLocalDate(startDate).plusDays(1);
		LocalDate endDateLd = asLocalDate(endDate).plusDays(1);

		// Cria array de dias entre data_inicio e data_fim
		ArrayList<LocalDate> arrayDiasNoIntervalo = dateIterator(startDateLd, endDateLd);

		List<DiaDaSemanaCode> diasDaSemanaArr = agenda.getDiaDaSemana();

		// Cria array de dias do diaDaSemana escolhido
		ArrayList<LocalDate> diasAgendarLDArray = new ArrayList<LocalDate>();
		for (int i = 0; i < diasDaSemanaArr.size(); i++) {
			String diaDaSemana = diasDaSemanaArr.get(i).name();
			ArrayList<LocalDate> diasAgendarLD = diasStoreLD(arrayDiasNoIntervalo, diaDaSemana);

			for (LocalDate da : diasAgendarLD) {
				diasAgendarLDArray.add(da);
			}
		}

		// Converte array de LocalDate para Date
		ArrayList<Date> diasAgendarArray = new ArrayList<>();
		for (int j = 0; j < diasAgendarLDArray.size(); j++) {
			diasAgendarArray.add(asDate(diasAgendarLDArray.get(j)));
		}

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
		if (agenda.getData_inicio() == null) {
			return false;
		}
		if (agenda.getData_fim() == null) {
			return false;
		}
		return true;
	}

	// private int buscaDiaDaSemana(Date dia) {
	// Calendar c = Calendar.getInstance();
	// c.setTime(dia);
	// int diaDaSemana = c.get(Calendar.DAY_OF_WEEK);
	// return diaDaSemana;
	// }

	// public static List<LocalDate> diasNoIntervalo(LocalDate startDate, LocalDate
	// endDate) {
	//
	// long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
	// return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(i ->
	// startDate.plusDays(i))
	// .collect(Collectors.toList());
	// }

	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public List<Date> loopDias(Date startDate, Date endDate) {
		List<Date> datesInRange = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		while (calendar.before(endCalendar)) {
			Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return datesInRange;
	}

	// private Date diaFormater(String date) {
	// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//
	// String dataString = sdf.format(date);
	// Date dataDate = null;
	//
	// try {
	// dataDate = sdf.parse(dataString);
	// } catch (ParseException e) {
	// System.out.println("CAGOU");
	// e.printStackTrace();
	// }
	//
	// return dataDate;
	// }

	private ArrayList<LocalDate> dateIterator(LocalDate startDateLd, LocalDate endDateLd) {
		ArrayList<LocalDate> arrayDias = new ArrayList<LocalDate>();
		for (LocalDate date = startDateLd; date.isBefore(endDateLd); date = date.plusDays(1)) {
			arrayDias.add(date);
		}
		return arrayDias;
	}

	private java.time.LocalDate asLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private ArrayList<LocalDate> diasStoreLD(ArrayList<LocalDate> arrayDias, String diaDaSemana) {

		ArrayList<LocalDate> diasPost = new ArrayList<LocalDate>();

		for (int i = 0; i < arrayDias.size(); i++) {
			if (diaDaSemana == arrayDias.get(i).getDayOfWeek().toString()) {
				diasPost.add(arrayDias.get(i));
			}
		}
		return diasPost;
	}

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

}
