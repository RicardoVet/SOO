package br.com.unesp.manageadbeans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.unesp.negocios.Comparar;

/**
 * 
 * Controller da tela de comparar, chamadas dos servi�os da tela
 * 
 *
 */
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

	/**
	 * Instancia o comparador
	 */
	@PostConstruct
	public void listarArquivos() {
		comparador = new Comparar();
	}

	/**
	 * Registra o indice do arquivo, para utiliza��o do Comparador e do
	 * Concatenador
	 * 
	 * @param nome
	 */
	public void addIndice(String nome) {
		try {
			String mensagem = comparador.addIndice(nome);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(comparador.getNomes().get(comparador.getNomes().indexOf(nome)) + mensagem));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de edi��o do arquivo
	 * 
	 * @param nome
	 */
	public void editar(String nome) {
		try {
			comparador.editar(nome);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de ordena��o do arquivo
	 * 
	 * @param nome
	 */
	public void ordenar(String nome) {
		try {
			comparador.ordenar(nome);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Arquivo Ordenado"));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o para apagar arquivo
	 * 
	 * @param nome
	 */
	public void apagar(String nome) {
		try {
			comparador.apagar(nome);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de download do arquivo
	 * 
	 * @param nome
	 */
	public void download(String nome) {
		try {
			comparador.download(nome);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de visualiza�� do arquivo
	 * 
	 * @param nome
	 */
	public void visualizar(String nome) {

		try {
			comparador.visualizar(nome);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de visualizar da tabela de compara��o
	 * 
	 * @param indice
	 * @return
	 */
	public String visualizarPorIndice(String indice) {
		try {
			return comparador.visualizarPorIndice(indice);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * Chama o servi�o de ler arquivo
	 * 
	 * @param dados
	 * @param nome
	 * @throws IOException
	 */
	public void lerArquivo(byte[] dados, String nome) throws IOException {
		try {
			comparador.lerArquivo(dados, nome);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * Chama o servi�o de gerar a bibKey
	 */
	public void gerarBibKey() {
		try {
			comparador.gerarBibKey();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Limpa os indices ao atualizar a pagina
	 * 
	 */
	public void checkF5() {
		if (comparador.getIndices() != null)
			comparador.getIndices().clear();
	}

	/**
	 * 
	 * Chama o servi�o de comparar arquivos
	 * 
	 */
	public void comparar() {
		try {
			comparador.comparar();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Chama o servi�o de concatenar arquivos
	 * 
	 */
	public void concatenar() {
		try {
			comparador.concatenar();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * Realiza o controle para mensagem de erro
	 * 
	 * 
	 * @param erro
	 */
	public void mostrarErro(String erro) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO:" + erro, erro);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
