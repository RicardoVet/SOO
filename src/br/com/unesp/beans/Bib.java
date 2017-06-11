package br.com.unesp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.unesp.servicos.Science;
import br.com.unesp.servicos.Scopus;

public class Bib extends Arquivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Bib() {
		setReferencias(new ArrayList<>());
		setAtributos(new HashMap<String, String>());
	}
/**    
 * criar logica de decisão para definir parser concreto
 * 
 * **/
	@Override
	public String parser(String conteudo) {
		parser = new Science();
		return parser.getParser(conteudo, this);
	}
}