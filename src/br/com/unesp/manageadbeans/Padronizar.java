package br.com.unesp.manageadbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.unesp.beans.Arquivo;

@ManagedBean
@RequestScoped
public class Padronizar {

	private StreamedContent file;
	
	@ManagedProperty(value = "#{compararArquivos}")
	CompararArquivos compararArquivos;

	private String caminho;

	public void setCompararArquivos(CompararArquivos compararArquivos) {
		this.compararArquivos = compararArquivos;
	}
    
	@PostConstruct
    public void init() {
    	List<List<Arquivo>> arquivos = compararArquivos.getArquivos();
    	List<Arquivo> arquivo = arquivos.get(0);
    	Arquivo bib = arquivo.get(0);
    	StringBuilder saida = new StringBuilder();
    	saida.append("@"+bib.getTipo()+"{"+bib.getReferencias()+",");
    	FileWriter writer;
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        caminho = scontext.getRealPath("/WEB-INF/upload/bib.txt");
		try {
			writer = new FileWriter(new File(scontext.getRealPath("/WEB-INF/upload/bib.txt")));
			writer.write(saida.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        InputStream stream;
		try {
			stream = new FileInputStream(scontext.getRealPath("/WEB-INF/upload/bib.txt"));
			file = new DefaultStreamedContent(stream, "application/txt", "teste.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	@PreDestroy
	public void finalize(){
        File file = new File(caminho);
        file.delete();
	}
 
    public StreamedContent getFile() {
        return file;
    }
}
