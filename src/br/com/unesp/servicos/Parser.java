package br.com.unesp.servicos;

import br.com.unesp.beans.Arquivo;

public interface Parser {

	public String getParser(String conteudo, Arquivo arq);
}
