package br.com.unesp.manageadbeans;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import br.com.unesp.beans.Arquivo;
import br.com.unesp.factory.FileFactory;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CompararArquivos implements Serializable {

	private List<String> nomes;
	private List<Integer> indices;
	private Arquivo arquivo;
	private List<Arquivo> arquivos;
	private List<String> filteredNomes;
	private String conteudo;

	@PostConstruct
	public void listarArquivos() {
		arquivos  = new ArrayList<>();
		nomes = new ArrayList<>();
		
	}

	public void addIndice(int indice) {
		if (indices == null)
			indices = new ArrayList<>();
		Object ret = indices.contains(indice) ? !indices.remove((Integer)indice) : indices.add(indice);
		String mensagem = (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice) + mensagem));
	}

	public void lerArquivo(byte[] dados) throws IOException {
		conteudo="";
		BufferedReader buffer= new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dados)));
		while(buffer.ready())
			conteudo+=buffer.readLine();
		buffer.close();
	}

	public void compararArquivos() {
		setArquivo(FileFactory.getInstance());
		arquivo.parser(conteudo);
		arquivos.add(arquivo);
	}

	public List<String> getNomes() {
		return nomes;
	}

	public void setNomes(List<String> nomes) {
		this.nomes = nomes;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public List<Integer> getIndices() {
		return indices;
	}

	public void setIndices(List<Integer> indices) {
		this.indices = indices;
	}

	public List<String> getFilteredNomes() {
		return filteredNomes;
	}

	public void setFilteredNomes(List<String> filteredNomes) {
		this.filteredNomes = filteredNomes;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
}
