package br.com.unesp.manageadbeans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.com.unesp.beans.Arquivo;
import br.com.unesp.constatntes.Const;
import br.com.unesp.factory.FileFactory;
import br.com.unesp.negocios.LeitorArquivos;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CompararArquivos implements Serializable {

	private List<String> nomes;
	private List<Integer> indices;
	private Arquivo arquivo;
	private List<Arquivo> arquivos;
	private List<String> filteredNomes;

	@PostConstruct
	public void listarArquivos() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		File dir = new File(scontext.getRealPath(Const.path.getValue()));
		if (!dir.list().equals("")) {
			nomes = new ArrayList<>();
			for (String nome : dir.list()) {
				nomes.add(nome);
			}
		}
	}

	public void addIndice(int indice) {
		if (indices == null)
			indices = new ArrayList<>();
		Object ret = indices.contains(indice) ? !indices.remove((Integer)indice) : indices.add(indice);
		String mensagem = (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice) + mensagem));
	}

	public void lerArquivo() {
		LeitorArquivos leitor = new LeitorArquivos();
		try {
			for (Integer indice : indices) {
				leitor.lerArquivo(FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath(Const.path.getValue() + nomes.get(indice)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compararArquivos() {
		this.arquivos = new ArrayList<>();
		for (Integer indice : indices) {
			setArquivo(FileFactory.getInstance());
			arquivo.parser(indices.indexOf(indice), nomes.get(indice));
			arquivos.add(arquivo);
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
}
