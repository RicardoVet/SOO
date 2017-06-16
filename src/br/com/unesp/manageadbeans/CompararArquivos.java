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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
	private List<String> filteredNomes;
	private String conteudo;
	int cont = 0;
	private String opcao;
	private String nome;
	private String saidaPadronizada;
	int i = 0;

	@PostConstruct
	public void listarArquivos() {
		arquivos = new ArrayList<>();
		nomes = new ArrayList<>();
		bib = new ArrayList<>();
	}

	public void addIndice(String nome) {
		if (indices == null)
			indices = new ArrayList<>();
		int indice = nomes.indexOf(nome);
		Object ret = indices.contains(indice) ? !indices.remove((Integer) indice) : indices.add(indice);
		String mensagem = (ret == Boolean.TRUE) ? " Adicionado" : " Removido";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice) + mensagem));
	}

	public void editar() {

	}

	public void apagar() {

	}

	public void download(String nome) {
		int indice = nomes.indexOf(nome);
		StringBuffer buffer = new StringBuffer();
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
				int tamanho = key.length();
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
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment;filename="+temp);
		response.setContentType("text/html; charset=UTF-8");
		ServletOutputStream out = null;
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(saidaPadronizada.getBytes());
			byte[] dados = new byte[(int)saidaPadronizada.length()];
			response.setContentLengthLong(saidaPadronizada.length());
	        out = response.getOutputStream();  
	        i  = 0;  
	        while ((i  = input.read(dados)) != -1) {  
	            out.write(dados);
	            out.flush();
	            FacesContext.getCurrentInstance().getResponseComplete();
	        }   
	    } catch (IOException err) {  
	        err.printStackTrace();  
	    } finally {  
	        try {  
	            if (out != null) {  
	                out.close();  
	            }  
	        } catch (IOException err) {  
	            err.printStackTrace();  
	        }  
	    }  
	}

	public void visualizar(String nome) {
		int indice = nomes.indexOf(nome);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nomes.get(indice)));
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
				int tamanho = key.length();
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
	}

	public void lerArquivo(byte[] dados, String opcao, String nome) throws IOException {
		conteudo = "";
		this.opcao = opcao;
		this.nome = nome;
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dados)));
		while (buffer.ready())
			conteudo += buffer.readLine();
		buffer.close();
		this.criarBib();
	}

	public void criarBib() {
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
	}

	public void checkF5() {
		if (indices != null)
			indices.clear();
	}

	public void comparar() {
		List<Arquivo> bibs1 = arquivos.get(indices.get(0));
		List<Arquivo> bibs2 = arquivos.get(indices.get(1));
		Arquivo bibView1 = FileFactory.getInstance();
		for (Arquivo bib1 : bibs1) {
			Set<Entry<String, String>> keysBib1 = bib1.getAtributos().entrySet();
			for (Arquivo bib2 : bibs2) {
				for (Entry<String, String> keyBib1 : keysBib1) {
					if (!bib2.getAtributos().containsKey(keyBib1.getKey().trim()))
						bibView1.getAtributos().put(keyBib1.getKey(), bib1.getAtributos().get(keyBib1.getKey()));
				}
			}
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

	public String getSaidaPadronizada() {
		return saidaPadronizada;
	}

	public void setSaidaPadronizada(String saidaPadronizada) {
		this.saidaPadronizada = saidaPadronizada;
	}
}
