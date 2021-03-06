package br.com.unesp.negocios;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import br.com.unesp.beans.Arquivo;
import br.com.unesp.constatntes.Constantes;
import br.com.unesp.factory.FileFactory;

/**
 * 
 * Classe com a logica para realizar a compara��o entre os arquivos e as bibTex
 * 
 * 
 *
 */
public class Comparar {

	private List<String> nomes;
	private List<Integer> indices;
	private Arquivo arquivo;
	private List<List<Arquivo>> arquivos;
	private List<Arquivo> bib;
	private List<Arquivo> bibUpdate;
	private List<String> filteredNomes;
	private String conteudo;
	int cont = 0;
	private String nome;
	private String saidaPadronizada;
	private Arquivo bibView1;
	int i = 0;

	public Comparar() {
		arquivos = new ArrayList<>();
		nomes = new ArrayList<>();
		bib = new ArrayList<>();
	}

	/**
	 * 
	 * Adiciona o arquivo selecionado em uma lista de indice
	 * 
	 * @param nome
	 * @return
	 */
	public String addIndice(String nome) {
		if (indices == null)
			indices = new ArrayList<>();
		int indice = nomes.indexOf(nome);
		Object ret = indices.contains(indice) ? !indices.remove((Integer) indice) : indices.add(indice);
		return (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
	}

	/**
	 * 
	 * Abre a janela de edi��o do arquivo
	 * 
	 * @param nome
	 */
	public void editar(String nome) {
		try {
			int indice = nomes.indexOf(nome);
			bibUpdate = arquivos.get(indice);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Ordena as bibTexs dentro do arquivo selecionado, pela bibKey
	 * 
	 * @param nome
	 */
	public void ordenar(String nome) {
		int indice = nomes.indexOf(nome);
		bibUpdate = arquivos.get(indice);
		for (int i = 0; i < bibUpdate.size(); i++) {
			for (int j = i + 1; j < bibUpdate.size(); j++) {
				if (bibUpdate.get(i).getReferencias().compareToIgnoreCase(bibUpdate.get(j).getReferencias()) > 0) {
					Arquivo bibTempI = bibUpdate.get(i);
					Arquivo bibTempJ = bibUpdate.get(j);
					bibUpdate.remove(i);
					bibUpdate.add(i, bibTempJ);
					bibUpdate.remove(j);
					bibUpdate.add(j, bibTempI);
				}
			}
		}
	}

	/**
	 * 
	 * Apaga o arquivo selecionado do indice e do programa
	 * 
	 * @param nome
	 */
	public void apagar(String nome) {
		try {
			int indice = nomes.indexOf(nome);
			arquivos.remove(indice);
			nomes.remove(indice);
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nome + " Removido"));
			FacesContext context = FacesContext.getCurrentInstance();
			String viewId = context.getViewRoot().getViewId();
			ViewHandler handler = context.getApplication().getViewHandler();
			UIViewRoot root = handler.createView(context, viewId);
			root.setViewId(viewId);
			context.setViewRoot(root);
			this.checkF5();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Realiza o download do arquivo para o usuario
	 * 
	 * @param nome
	 */
	public void download(String nome) {
		try {
			int indice = nomes.indexOf(nome);
			StringBuffer buffer = new StringBuffer();
			List<Arquivo> bibTex = arquivos.get(indice);
			for (Arquivo bib : bibTex) {
				buffer.append("@");
				buffer.append(bib.getTipo());
				buffer.append("{");
				buffer.append(bib.getReferencias().toString().replace("[", "").replace("]", ""));
				buffer.append(",");
				buffer.append("\n");
				Set<String> keys = bib.getAtributos().keySet();
				for (String key : keys) {
					buffer.append("  ");
					buffer.append(key.trim());
					int tamanho = key.trim().length();
					while (tamanho < 16) {
						buffer.append(" ");
						tamanho++;
					}
					buffer.append("=");
					buffer.append(" ");
					buffer.append("{");
					buffer.append(bib.getAtributos().get(key));
					buffer.append("},");
					buffer.append("\n");
				}
				buffer.append("\n");
			}
			setSaidaPadronizada(buffer.toString());

			StringBuffer temp = new StringBuffer(nome);
			temp.toString().replace(".bib", ".txt");

			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.setHeader("Content-Disposition", "attachment;filename=" + temp);
			response.setContentType("text/html; charset=UTF-8");
			ServletOutputStream out = null;
			try {
				ByteArrayInputStream input = new ByteArrayInputStream(saidaPadronizada.getBytes());
				byte[] dados = new byte[(int) saidaPadronizada.length()];
				response.setContentLengthLong(saidaPadronizada.length());
				out = response.getOutputStream();
				i = 0;
				while ((i = input.read(dados)) != -1) {
					out.write(dados);
					out.flush();
					FacesContext.getCurrentInstance().getResponseComplete();
				}
			} catch (IOException err) {
				throw new Exception(err.getMessage());
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException err) {
					throw new Exception(err.getMessage());
				}
			}
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Abre a janela de visualiza��o do arquivo selecionado
	 * 
	 * @param nome
	 */
	public void visualizar(String nome) {

		try {
			int indice = nomes.indexOf(nome);
			StringBuffer buffer = new StringBuffer();
			List<Arquivo> bibTex = arquivos.get(indice);
			for (Arquivo bib : bibTex) {
				buffer.append("@");
				buffer.append(bib.getTipo());
				buffer.append("{");
				buffer.append(bib.getReferencias());
				buffer.append(",");
				buffer.append(Constantes.novaLinha);
				Set<String> keys = bib.getAtributos().keySet();
				for (String key : keys) {
					buffer.append(Constantes.espaco + Constantes.espaco);
					buffer.append(key.trim());
					int tamanho = key.trim().length();
					while (tamanho < 16) {
						buffer.append(Constantes.espaco);
						tamanho++;
					}
					buffer.append("=");
					buffer.append(Constantes.espaco);
					buffer.append("{");
					buffer.append(bib.getAtributos().get(key));
					buffer.append("},");
					buffer.append(Constantes.novaLinha);
				}
				buffer.append(Constantes.novaLinha);
			}
			setSaidaPadronizada(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Devolve os arquivos conforme o indice no formato de html para exibi��o
	 * 
	 * @param indice
	 * @return
	 */
	public String visualizarPorIndice(String indice) {
		StringBuffer buffer = new StringBuffer();
		int index = indices.get(Integer.valueOf(indice));
		List<Arquivo> bibTex = arquivos.get(index);
		for (Arquivo bib : bibTex) {
			buffer.append("@");
			buffer.append(bib.getTipo());
			buffer.append("{");
			buffer.append(bib.getReferencias());
			buffer.append(",");
			buffer.append(Constantes.novaLinha);
			Set<String> keys = bib.getAtributos().keySet();
			for (String key : keys) {
				buffer.append(Constantes.espaco + Constantes.espaco);
				buffer.append(key.trim());
				int tamanho = key.trim().length();
				while (tamanho < 16) {
					buffer.append(Constantes.espaco);
					tamanho++;
				}
				buffer.append("=");
				buffer.append(Constantes.espaco);
				buffer.append("{");
				buffer.append(bib.getAtributos().get(key));
				buffer.append("},");
				buffer.append(Constantes.novaLinha);
			}
			buffer.append(Constantes.novaLinha);
		}
		setSaidaPadronizada(buffer.toString());
		return saidaPadronizada;

	}

	/**
	 * 
	 * Converte o array de bytes com as informa��es do arquivo para uma String
	 * em java.
	 * 
	 * @param dados
	 * @param nome
	 * @throws IOException
	 */
	public void lerArquivo(byte[] dados, String nome) throws IOException {
		try {
			conteudo = "";
			this.nome = nome;
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dados)));
			while (buffer.ready())
				conteudo += buffer.readLine();
			buffer.close();
			this.criarBib();
		} catch (Exception e) {
			e.printStackTrace();
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Gera o objeto Bib, utilizando a fabrica
	 * 
	 */
	private void criarBib() {
		String result[] = conteudo.split("@");
		for (int i = 1; i < result.length; i++) {
			setArquivo(FileFactory.getInstance());
			arquivo.parser(result[i]);
			bib.add(arquivo);
		}

		if (nomes.contains(nome)) {
			int posicao = nomes.indexOf(nome);
			arquivos.remove(posicao);
			arquivos.add(posicao, bib);
		} else {
			nomes.add(nome);
			arquivos.add(bib);
		}
		bib = new ArrayList<>();
	}

	/**
	 * 
	 * Apos ordena��o, verifica bibKey iguais, e caso tenha igual, concatena a
	 * letra
	 * 
	 */
	private void verificarDuplicidade() {
		for (int indice = 0; indice < arquivos.size(); indice++) {
			bibUpdate = arquivos.get(indice);
			for (int i = 0; i < bibUpdate.size(); i++) {
				char letra = 'a';
				for (int j = i + 1; j < bibUpdate.size(); j++) {
					if (bibUpdate.get(i).getReferencias().equals(bibUpdate.get(j).getReferencias())) {
						bibUpdate.get(j).setReferencias(bibUpdate.get(j).getReferencias() + letra);
						letra++;
					}
				}
			}
		}
	}

	/**
	 * 
	 * Gera bibKey de todos os arquivos de acordo com as regras passadas pelo
	 * professor
	 * 
	 */
	public void gerarBibKey() {
		try {
			for (List<Arquivo> bib : arquivos) {
				StringBuffer autor = new StringBuffer();
				StringBuffer ano = new StringBuffer();
				Arquivo bibTexTemp;
				for (Arquivo bibTex : bib) {
					bibTexTemp = bibTex;
					Set<Entry<String, String>> keysAndValuesBib = bibTex.getAtributos().entrySet();
					for (Entry<String, String> keyAndValue : keysAndValuesBib) {
						if (keyAndValue.getKey().equalsIgnoreCase("author")) {
							String[] nomes = keyAndValue.getValue().split("and|AND");
							String sobreNome;
							int cont = 0;
							for (String nome : nomes) {
								if (cont == 0) {
									String[] temp = nome.trim().split("\\s+");
									sobreNome = temp[0].trim();
									autor.append(sobreNome.toLowerCase());
								}

								if (cont == 1) {
									String[] temp = nome.trim().split("\\s+");
									sobreNome = temp[0].trim();
									autor.append(".");
									autor.append(sobreNome.toLowerCase());
								}

								if (cont == 2) {
									String[] temp = nome.split("\\s+");
									sobreNome = temp[0].trim();
									autor.replace(autor.indexOf(".") + 1, autor.length(), "");
									autor.append("etal");
								}
								cont++;
							}
						}
						if (keyAndValue.getKey().equalsIgnoreCase("year")) {
							ano.append(keyAndValue.getValue());
						}
					}
					bibTexTemp.setReferencias(autor.toString().replace(",", "") + ":" + ano.toString());
					autor = new StringBuffer();
					ano = new StringBuffer();
				}
			}
			this.verificarDuplicidade();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("BibKey gerada com sucesso"));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Abre a janela de compara��o, pega os arquivos selecionados, e devolve as
	 * diferen�as
	 * 
	 */
	public void comparar() {
		try {
			List<Arquivo> bibs1 = arquivos.get(indices.get(0));
			List<Arquivo> bibs2 = arquivos.get(indices.get(1));
			bibView1 = FileFactory.getInstance();
			for (Arquivo bib1 : bibs1) {
				Set<Entry<String, String>> keysBib1 = bib1.getAtributos().entrySet();
				for (Arquivo bib2 : bibs2) {
					for (Entry<String, String> keyBib1 : keysBib1) {
						if (!bib2.getAtributos().containsKey(keyBib1.getKey().trim()))
							bibView1.getAtributos().put(keyBib1.getKey(), bib1.getAtributos().get(keyBib1.getKey()));
					}
				}
			}
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	/**
	 * 
	 * Concatena os arquivos selecionados em um terceiro arquivo que ser�
	 * enviado para download
	 * 
	 */
	public void concatenar() {
		try {
			StringBuffer buffer = new StringBuffer();
			StringBuffer nomeConcatenado = new StringBuffer();
			for (int indice : indices) {
				nomeConcatenado.append(nomes.get(indice));
				List<Arquivo> bibTex = arquivos.get(indice);
				for (Arquivo bib : bibTex) {
					buffer.append("@");
					buffer.append(bib.getTipo());
					buffer.append("{");
					buffer.append(bib.getReferencias());
					buffer.append(",");
					buffer.append("\n");
					Set<String> keys = bib.getAtributos().keySet();
					for (String key : keys) {
						buffer.append("  ");
						buffer.append(key.trim());
						int tamanho = key.trim().length();
						while (tamanho < 16) {
							buffer.append(" ");
							tamanho++;
						}
						buffer.append("=");
						buffer.append(" ");
						buffer.append("{");
						buffer.append(bib.getAtributos().get(key));
						buffer.append("},");
						buffer.append("\n");
					}
					buffer.append("\n");
				}
				nomeConcatenado.append("+");
			}
			setSaidaPadronizada(buffer.toString());
			nomeConcatenado.replace(nomeConcatenado.lastIndexOf("+"), nomeConcatenado.lastIndexOf("+") + 1, "")
					.append(".txt");

			RequestContext context = RequestContext.getCurrentInstance();
			context.release();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.setHeader("Content-Disposition", "attachment;filename=" + nomeConcatenado);
			response.setContentType("text/html; charset=UTF-8");
			ServletOutputStream out = null;
			try {
				ByteArrayInputStream input = new ByteArrayInputStream(saidaPadronizada.getBytes());
				byte[] dados = new byte[(int) saidaPadronizada.length()];
				response.setContentLengthLong(saidaPadronizada.length());
				out = response.getOutputStream();
				i = 0;
				while ((i = input.read(dados)) != -1) {
					out.write(dados);
					out.flush();
					FacesContext.getCurrentInstance().getResponseComplete();
				}
			} catch (IOException err) {
				throw new Exception(err.getMessage());
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException err) {
					throw new Exception(err.getMessage());
				}
			}
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
		if (indices != null)
			indices.clear();
	}

	/**
	 * 
	 * Controle de erros
	 * 
	 * @param erro
	 */
	public void mostrarErro(String erro) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO:" + erro, erro);
		FacesContext.getCurrentInstance().addMessage(null, message);
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

	public List<Arquivo> getBibUpdate() {
		return bibUpdate;
	}

	public void setBibUpdate(List<Arquivo> bibUpdate) {
		this.bibUpdate = bibUpdate;
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

	public String getSaidaPadronizada() {
		return saidaPadronizada;
	}

	public void setSaidaPadronizada(String saidaPadronizada) {
		this.saidaPadronizada = saidaPadronizada;
	}

	public Arquivo getBibView1() {
		return bibView1;
	}

	public void setBibView1(Arquivo bibView1) {
		this.bibView1 = bibView1;
	}
}
