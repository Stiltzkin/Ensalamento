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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.uniopet.webservice.model.domain.Agenda;
import br.edu.uniopet.webservice.resources.beans.PaginationFilterBean;
import br.edu.uniopet.webservice.service.AgendaService;

@Path("/agendas")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AgendaResource {
	private AgendaService service = new AgendaService();
	
	@GET
	public List<Agenda> getAgendas(@BeanParam PaginationFilterBean paginateFilter){
		if((paginateFilter.getQtd() >= 0) && (paginateFilter.getPg() > 0)) {
			return service.getByAgendaPagination(paginateFilter.getPg(), paginateFilter.getQtd());
		}
		return service.getAll();
	}
	
	@GET
	@Path("{agendaId}")	
	public Agenda getAgenda(@PathParam("agendaId") Long idAgenda) {
		return service.getById(idAgenda);
	}
	
	@POST
	public Response save(Agenda agenda) {
		agenda = service.save(agenda);
		return Response.status(Status.CREATED).entity(agenda).build();
	}
	
	@PUT
	@Path("{agendaId}")
	public void update(@PathParam("agendaId") Long idAgenda, Agenda agenda) {
		agenda.setIdAgenda(idAgenda);
		service.update(agenda);
	}
	
	@DELETE
	@Path("{agendaId}")
	public void delete(@PathParam("agendaId") Long idAgenda) {
		service.delete(idAgenda);
	}

}
