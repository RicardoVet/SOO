package br.com.unesp.manageadbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.unesp.beans.Arquivo;
import br.com.unesp.beans.Bib;

@ManagedBean
@RequestScoped
public class CadastrarMB {

	private Arquivo arquivo;
	
	@PostConstruct
	public void init(){
		List<String> ref = new ArrayList<>();
		ref.add("Ricardo:2017");
		arquivo = new Bib();
	}
	
	public String cadastrar(){
		FacesMessage message = new FacesMessage("Sucesso", arquivo.getReferencias() + " cadastrado ");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
}
