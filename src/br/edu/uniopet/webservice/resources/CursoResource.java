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

import br.edu.uniopet.webservice.model.domain.Curso;
import br.edu.uniopet.webservice.resources.beans.PaginationFilterBean;
import br.edu.uniopet.webservice.service.CursoService;

@Path("/cursos")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CursoResource {
	private CursoService service = new CursoService();

	@GET
	@Path("{cursoId}")
	public Curso getCurso(@PathParam("cursoId") Long id) {
		return service.getCurso(id);
	}
	
	@GET
	public List<Curso> getCurso(@BeanParam PaginationFilterBean paginateFilter){
		if((paginateFilter.getPg() >= 0) && (paginateFilter.getQtd() > 0)) {
			return service.getCursoByPagination(paginateFilter.getPg(), paginateFilter.getQtd());
		}
		if(paginateFilter.getName() != null) {
			return service.getCursoByName(paginateFilter.getName());
		}
		return service.getCursos();
	}

	@POST
	public Response save(Curso curso) {
		curso = service.saveCurso(curso);
		return Response.status(Status.CREATED).entity(curso).build();
	}

	@PUT
	@Path("{cursoId}")
	public void update(@PathParam("cursoId") Long id, Curso curso) {
		curso.setId(id);
		service.updateCurso(curso);
	}

	@DELETE
	@Path("{cursoId}")
	public void delete(@PathParam("cursoId") Long id) {
		service.deleteCurso(id);
	}
}
