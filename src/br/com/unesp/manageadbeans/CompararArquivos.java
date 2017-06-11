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
	private List<List<Arquivo>> arquivos;
	private List<Arquivo> bib;
	private List<String> filteredNomes;
	private String conteudo;
	int cont = 0;

	@PostConstruct
	public void listarArquivos() {
		arquivos = new ArrayList<>();
		nomes = new ArrayList<>();
		bib = new ArrayList<>();
	}

	public void addIndice(int indice) {
		if (indices == null)
			indices = new ArrayList<>();
		Object ret = indices.contains(indice) ? !indices.remove((Integer) indice) : indices.add(indice);
		String mensagem = (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice) + mensagem));
	}

	public void lerArquivo(byte[] dados) throws IOException {
		conteudo = "";
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dados)));
		while (buffer.ready())
			conteudo += buffer.readLine();
		buffer.close();
		this.compararArquivos();
	}

	public void compararArquivos() {
		String temp = "";
		setArquivo(FileFactory.getInstance());
		temp = arquivo.parser(conteudo);
		bib.add(arquivo);
		if (!temp.equals("")) {
			cont++;
			conteudo = "";
			conteudo = temp;
			compararArquivos();
			cont--;
		}
		if (cont == 0){
			arquivos.add(bib);
			bib = new ArrayList<>();
		}
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

	public List<List<Arquivo>> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<List<Arquivo>> arquivos) {
		this.arquivos = arquivos;
	}

	public List<Arquivo> getBib() {
		return bib;
	}

	public void setBib(List<Arquivo> bib) {
		this.bib = bib;
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
