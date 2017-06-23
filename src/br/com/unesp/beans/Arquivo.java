package br.com.unesp.beans;

import java.util.Map;

import br.com.unesp.servicos.Parser;

/**
 * 
 * Classe Arquivo
 * 
 * Classe abstrata para o arquivo .bib
 *
 */
public abstract class Arquivo {

	private String referencias;
	private Map<String, String>atributos;
	private String tipo;
	protected Parser parser;
	
	public abstract void parser(String couteudo);
	
	
	public Map<String, String> getAtributos() {
		return atributos;
	}

	public void setAtributos(Map<String, String> atributos) {
		this.atributos = atributos;
	}

	public String getTipo() {
		return tipo;
	}

	public String getReferencias() {
		return referencias;
	}


	public void setReferencias(String referencias) {
		this.referencias = referencias;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
