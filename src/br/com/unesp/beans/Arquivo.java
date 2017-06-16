package br.com.unesp.beans;

import java.util.List;
import java.util.Map;

import br.com.unesp.servicos.Parser;

public abstract class Arquivo {

	private List<String> referencias;
	private Map<String, String>atributos;
	private String tipo;
	protected Parser parser;
	
	public abstract String parser(String couteudo, String opcao);
	
	
	public Map<String, String> getAtributos() {
		return atributos;
	}

	public void setAtributos(Map<String, String> atributos) {
		this.atributos = atributos;
	}

	public void setReferencias(List<String> referencias) {
		this.referencias = referencias;
	}
	
	public List<String> getReferencias() {
		return referencias;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
