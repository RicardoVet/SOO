package br.com.unesp.servicos;

import br.com.unesp.beans.Arquivo;

/**
 * 
 * Interface do parser
 * 
 */
public interface Parser {

	public String getParser(String conteudo, Arquivo arq);
}
