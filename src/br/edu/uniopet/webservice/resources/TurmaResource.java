package br.edu.uniopet.webservice.resources;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;

import br.edu.uniopet.webservice.model.domain.Turma;
import br.edu.uniopet.webservice.resources.beans.PaginationFilterBean;
import br.edu.uniopet.webservice.service.TurmaService;

@Path("/turmas")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TurmaResource {

	private TurmaService service = new TurmaService();

	@GET
	public List<Turma> getTurmas(@BeanParam PaginationFilterBean paginateFilter) {
		if ((paginateFilter.getPg() >= 0) && (paginateFilter.getQtd() > 0)) {
			return service.getTurmaByPagination(paginateFilter.getPg(), paginateFilter.getQtd());
		}
		if (paginateFilter.getName() != null) {
			return service.getTurmaByName(paginateFilter.getName());
		}

		return service.getTurmas();
	}

	@GET
	@Path("{turmaId}")
	public Turma getTurma(@PathParam("turmaId") Long idTurma) {
		return service.getTurma(idTurma);
	}

	@POST
	public Response save(Turma turma) {
		turma = service.saveTurma(turma);
		return Response.status(Status.CREATED).entity(turma).build();
	}

	@PUT
	@Path("{turmaId}")
	public void update(@PathParam("turmaId") Long idTurma, Turma turma) {
		turma.setIdTurma(idTurma);
		service.updateTurma(turma);
	}

	@DELETE
	@Path("{turmaId}")
	public void delete(@PathParam("turmaId") Long idTurma) {
		service.deleteTurma(idTurma);
	}
}
