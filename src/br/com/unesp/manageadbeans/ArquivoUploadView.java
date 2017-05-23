package br.com.unesp.manageadbeans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@RequestScoped
public class ArquivoUploadView {
	
	@ManagedProperty(value = "#{compararArquivos}")
	CompararArquivos compararArquivos;

	public void setCompararArquivos(CompararArquivos compararArquivos) {
		this.compararArquivos = compararArquivos;
	}

	public void upload(FileUploadEvent event){
		compararArquivos.getNomes().add(event.getFile().getFileName());
		try {
			compararArquivos.lerArquivo(event.getFile().getContents());
		} catch (IOException e) {
			FacesMessage message = new FacesMessage("Erro", event.getFile().getFileName() + " is not uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			return;
		}
		FacesMessage message = new FacesMessage("Sucesso", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
