package br.com.unesp.manageadbeans;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import br.com.unesp.beans.Arquivo;
import br.com.unesp.constatntes.Constantes;
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
	private List<Arquivo> bibUpdate;
	private List<String> filteredNomes;
	private String conteudo;
	int cont = 0;
	private String opcao;
	private String nome;
	private String saidaPadronizada;
	private Arquivo bibView1;
	int i = 0;

	@PostConstruct
	public void listarArquivos() {
		arquivos = new ArrayList<>();
		nomes = new ArrayList<>();
		bib = new ArrayList<>();
	}

	public void addIndice(String nome) {

		try {
			if (indices == null)
				indices = new ArrayList<>();
			int indice = nomes.indexOf(nome);
			Object ret = indices.contains(indice) ? !indices.remove((Integer) indice) : indices.add(indice);
			String mensagem = (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice) + mensagem));
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public Boolean valueCheck(String nome) {
		if (indices != null) {
			int indice = nomes.indexOf(nome);
			if (indices.contains(indice))
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void editar(String nome) {
		try {
			int indice = nomes.indexOf(nome);
			bibUpdate = arquivos.get(indice);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void apagar(String nome) {
		try {
			int indice = nomes.indexOf(nome);
			arquivos.remove(indice);
			nomes.remove(indice);
			if (indices != null)
				indices.remove(indice);
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nome+" Removido"));
			FacesContext context = FacesContext.getCurrentInstance();
			String viewId = context.getViewRoot().getViewId();
			ViewHandler handler = context.getApplication().getViewHandler();
			UIViewRoot root = handler.createView(context, viewId);
			root.setViewId(viewId);
			context.setViewRoot(root);
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

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
			mostrarErro(e.getMessage());
		}
	}

	public String visualizarPorIndice(String indice) {
		try {
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
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
		return saidaPadronizada;

	}

	public void lerArquivo(byte[] dados, String opcao, String nome) throws IOException {
		try {
			conteudo = "";
			this.opcao = opcao;
			this.nome = nome;
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dados)));
			while (buffer.ready())
				conteudo += buffer.readLine();
			buffer.close();
			this.criarBib();
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void criarBib() {
		try {
			String temp = "";
			setArquivo(FileFactory.getInstance());
			temp = arquivo.parser(conteudo, opcao);
			bib.add(arquivo);
			if (!temp.equals("")) {
				cont++;
				conteudo = "";
				conteudo = temp;
				criarBib();
				cont--;
			}
			if (cont == 0) {
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
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void gerarBibKey() {
		try {
			for (List<Arquivo> bib : arquivos) {
				for (Arquivo bibTex : bib) {
					Set<Entry<String, String>> keysAndValuesBib = bibTex.getAtributos().entrySet();
					for (Entry<String, String> keyAndValue : keysAndValuesBib) {
						if (keyAndValue.getKey().equalsIgnoreCase("author")) {
							System.out.println(keyAndValue.getValue());
						}
						if (keyAndValue.getKey().equalsIgnoreCase("year")) {
							System.out.println(keyAndValue.getValue());
						}
					}
				}
			}
		} catch (Exception e) {
			mostrarErro(e.getMessage());
		}
	}

	public void checkF5() {
		if (indices != null)
			indices.clear();
	}

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
