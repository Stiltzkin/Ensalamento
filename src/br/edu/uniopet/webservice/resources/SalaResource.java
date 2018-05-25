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

import br.edu.uniopet.webservice.model.domain.Sala;
import br.edu.uniopet.webservice.resources.beans.PaginationFilterBean;
import br.edu.uniopet.webservice.service.SalaService;

@Path("/salas")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SalaResource {
	private SalaService service = new SalaService();

	@GET
	public List<Sala> getSalas(@BeanParam PaginationFilterBean paginateFilter) {
		if (paginateFilter.getPg() >= 0 && paginateFilter.getQtd() >= 0) {
			return service.getSalaByPagination(paginateFilter.getPg(), paginateFilter.getQtd());
		}
		if (paginateFilter.getName() != null) {
			return service.getSalaByName(paginateFilter.getName());
		}
		return service.getAll();
	}

	@GET
	@Path("{salaId}")
	public Sala getSala(@PathParam("salaId") Long idSala) {
		return service.getSalaById(idSala);
	}
	
	@POST
	public Response save(Sala sala) {
		sala = service.save(sala);
		return Response.status(Status.CREATED).entity(sala).build();
	}
	
	@PUT
	@Path("{salaId}")
	public void update(@PathParam("salaId") Long idSala, Sala sala) {
		sala.setIdSala(idSala);
		service.update(sala);
	}
	
	@DELETE
	@Path("{salaId}")
	public void delete(@PathParam("salaId") Long idSala) {
		service.delete(idSala);
	}
}
