package br.edu.uniopet.webservice.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.uniopet.webservice.model.domain.Agenda;
import br.edu.uniopet.webservice.model.domain.DiaDaSemanaCode;

public class AgendamentoUtil {
	public ArrayList<Date> diasSelecionados(Agenda agenda){
		LocalDate startDateLd = asLocalDate(agenda.getData_inicio());
		LocalDate endDateLd = asLocalDate(agenda.getData_fim());

		// Cria array de dias entre data_inicio e data_fim
		ArrayList<LocalDate> arrayDiasNoIntervalo = dateIterator(startDateLd, endDateLd);

		// Cria array das semanas no enum DiaDaSemanaCode
		List<DiaDaSemanaCode> diasDaSemanaArr = agenda.getDiaDaSemana();

		// Cria array de dias do diaDaSemana escolhido
		ArrayList<LocalDate> diasAgendarLDArray = diasEntreInicioFim(diasDaSemanaArr, arrayDiasNoIntervalo);

		// Converte array de LocalDate para Date
		ArrayList<Date> diasAgendarArray = new ArrayList<>();
		for (int j = 0; j < diasAgendarLDArray.size(); j++) {
			diasAgendarArray.add(asDate(diasAgendarLDArray.get(j)));
		}
		return diasAgendarArray;
	}
	
	
	private java.time.LocalDate asLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	private ArrayList<LocalDate> dateIterator(LocalDate startDateLd, LocalDate endDateLd) {
		ArrayList<LocalDate> arrayDias = new ArrayList<LocalDate>();
		for (LocalDate date = startDateLd; date.isBefore(endDateLd); date = date.plusDays(1)) {
			arrayDias.add(date);
		}
		return arrayDias;
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

	private ArrayList<LocalDate> diasEntreInicioFim(List<DiaDaSemanaCode> diasDaSemanaArr,
			ArrayList<LocalDate> arrayDiasNoIntervalo) {
		ArrayList<LocalDate> diasAgendarLDArray = new ArrayList<LocalDate>();
		for (int i = 0; i < diasDaSemanaArr.size(); i++) {
			String diaDaSemana = diasDaSemanaArr.get(i).name();
			ArrayList<LocalDate> diasAgendarLD = diasStoreLD(arrayDiasNoIntervalo, diaDaSemana);

			for (LocalDate da : diasAgendarLD) {
				diasAgendarLDArray.add(da);
			}
		}
		return diasAgendarLDArray;
	}
}
