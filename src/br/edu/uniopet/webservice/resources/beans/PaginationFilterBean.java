package br.edu.uniopet.webservice.resources.beans;

import javax.ws.rs.QueryParam;

public class PaginationFilterBean {

	private @QueryParam("pg") int pg;
	private @QueryParam("qtd") int qtd;
	private @QueryParam("name") String name;
	
	public int getPg() {
		return pg;
	}
	public void setPg(int pg) {
		this.pg = pg;
	}
	public int getQtd() {
		return qtd;
	}
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
