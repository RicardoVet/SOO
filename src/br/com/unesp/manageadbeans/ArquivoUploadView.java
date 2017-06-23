package br.com.unesp.manageadbeans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;


/**
 * 
 * Gerencia os dados do arquivo em um array de bites
 * 
 *
 */
@ManagedBean
@ViewScoped
public class ArquivoUploadView {
	
	@ManagedProperty(value = "#{compararArquivos}")
	CompararArquivos compararArquivos;

	public void setCompararArquivos(CompararArquivos compararArquivos) {
		this.compararArquivos = compararArquivos;
	}
	
	
	/**
	 * 
	 * Realiza o upload o arquivo, passando as informações para um array de bites.
	 * Logo em seguida, realiza injeção de dependencia no CompararArquivos
	 * 
	 * @param event
	 */
	public void upload(FileUploadEvent event){
		String nome = event.getFile().getFileName();
		byte[] dados = event.getFile().getContents();
		try {
			compararArquivos.lerArquivo(dados, nome);
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
