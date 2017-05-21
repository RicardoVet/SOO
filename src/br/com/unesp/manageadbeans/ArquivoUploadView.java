package br.com.unesp.manageadbeans;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.primefaces.event.FileUploadEvent;

import br.com.unesp.negocios.UploadArquivo;

@ManagedBean
@RequestScoped
public class ArquivoUploadView {
	
	private UploadArquivo upload;
	private File caminho;
	
	@PostConstruct
	public void init(){
		upload=new UploadArquivo();
	}
	
	public String clean(){
		try {
			FileUtils.cleanDirectory(caminho);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Destruido com sucsso");
		return "inicio";
	}

	public void upload(FileUploadEvent event) {
		 byte[] dados = event.getFile().getContents();
	        String nomeArquivo = event.getFile().getFileName();
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
	        String arquivo = scontext.getRealPath("/WEB-INF/upload/" + nomeArquivo);
	        caminho = new File(scontext.getRealPath("/WEB-INF/upload/"));
	        try {
				upload.salvar(dados, nomeArquivo, arquivo);
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
