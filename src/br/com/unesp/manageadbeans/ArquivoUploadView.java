package br.com.unesp.manageadbeans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@ViewScoped
public class ArquivoUploadView {
	
	private String opcao;
	
	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	@ManagedProperty(value = "#{compararArquivos}")
	CompararArquivos compararArquivos;

	public void setCompararArquivos(CompararArquivos compararArquivos) {
		this.compararArquivos = compararArquivos;
	}
	
	public void upload(FileUploadEvent event){
		String nome = event.getFile().getFileName();
		byte[] dados = event.getFile().getContents();
		try {
			compararArquivos.lerArquivo(dados, opcao, nome);
		} catch (IOException e) {
			FacesMessage message = new FacesMessage("Erro", nome + " não foi enviado.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			return;
		}
		FacesMessage message = new FacesMessage("Sucesso", nome + " foi enviado.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
