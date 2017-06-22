package br.com.unesp.manageadbeans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.unesp.negocios.Comparar;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CompararArquivos implements Serializable {

	private Comparar comparador;

	public Comparar getComparador() {
		return comparador;
	}

	public void setComparador(Comparar comparador) {
		this.comparador = comparador;
	}

	@PostConstruct
	public void listarArquivos() {
		comparador = new Comparar();
	}

	public void addIndice(String nome) {
		try {
			String mensagem = comparador.addIndice(nome);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(comparador.getNomes().get(comparador.getNomes().indexOf(nome)) + mensagem));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void editar(String nome) {
		try {
			comparador.editar(nome);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void ordenar(String nome) {
		try{
		comparador.ordenar(nome);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Arquivo Ordenado"));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void apagar(String nome) {
		try {
			comparador.apagar(nome);
			} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void download(String nome) {
		try {
			comparador.download(nome);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void visualizar(String nome) {

		try {
			comparador.visualizar(nome);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	public String visualizarPorIndice(String indice) {
		try {
			return comparador.visualizarPorIndice(indice);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
		return null;
	}

	public void lerArquivo(byte[] dados, String nome) throws IOException {
		try {
			comparador.lerArquivo(dados, nome);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	public void gerarBibKey() {
		try {
			comparador.gerarBibKey();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void checkF5() {
		if (comparador.getIndices() != null)
			comparador.getIndices().clear();
	}

	public void comparar() {
		try {
			comparador.comparar();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void concatenar() {
		try {
			comparador.concatenar();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void mostrarErro(String erro) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO:" + erro, erro);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
