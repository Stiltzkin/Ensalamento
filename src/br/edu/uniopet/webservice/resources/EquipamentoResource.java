package br.edu.uniopet.webservice.resources;

import java.util.List;

import javax.websocket.server.PathParam;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.uniopet.webservice.model.domain.Equipamento;
import br.edu.uniopet.webservice.resources.beans.PaginationFilterBean;
import br.edu.uniopet.webservice.service.EquipamentoService;

@Path("/equipamentos")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class EquipamentoResource {
	private EquipamentoService service = new EquipamentoService();

	@GET
	@Path("{equipamentoId}")
	public Equipamento getEquipamento(@PathParam("equipamentoId") Long id) {
		return service.getEquipamento(id);
	}

	@GET
	public List<Equipamento> getEquipamento(@BeanParam PaginationFilterBean paginateFilter) {
		if ((paginateFilter.getPg() >= 0) && (paginateFilter.getQtd() > 0)) {
			return service.getEquipamentoByPagination(paginateFilter.getPg(), paginateFilter.getQtd());
		}
		if (paginateFilter.getName() != null) {
			return service.getEquipamentoByName(paginateFilter.getName());
		}
		return service.getAll();
	}

	@POST
	public Equipamento save(Equipamento equipamento) {
		return service.saveEquipamento(equipamento);
	}

//	@PUT
//	@Path("{equipamentoId}")
//	public void update(@PathParam("equipamentoId") Long id, Equipamento equipamento) {
//		equipamento.setIdEquipamento(id);
//		service.updateEquipamento(equipamento);
//	}

	@DELETE
	@Path("{equipamentoId}")
	public void delete(@PathParam("equipamentoId") Long id) {
		service.deleteEquipamento(id);
	}
}
