package br.com.unesp.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;
import br.com.unesp.servicos.Explore;
import br.com.unesp.servicos.Science2;
import br.com.unesp.servicos.Science3;
import br.com.unesp.servicos.Springer;

public class Bib extends Arquivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Bib() {
		setAtributos(new HashMap<String, String>());
	}

	/**
	 * criar logica de decisão para definir parser concreto
	 * 
	 **/
	@Override
	public void parser(String conteudo) {
		Pattern pattern = Pattern.compile("(\\w|\\W)*(}\\s*})");
		Pattern pattern2 = Pattern.compile("(\\w|\\W)*(}\\s*,*\\s*})");
		Pattern pattern3 = Pattern.compile("(\\w|\\W)*(\"\\s*})");
		Pattern pattern4 = Pattern.compile("(\\w|\\W)*(\"\\s*,*\\s*})");
		if (pattern.matcher(conteudo).find()) {
			parser = new Springer();
			parser.getParser(conteudo, this);
		} else if (pattern2.matcher(conteudo).find()) {
			parser = new Explore();
			parser.getParser(conteudo, this);
		} else if (pattern3.matcher(conteudo).find()) {
			parser = new Science2();
			parser.getParser(conteudo, this);
		} else if (pattern4.matcher(conteudo).find()) {
			parser = new Science3();
			parser.getParser(conteudo, this);
		} else {
			System.out.println(conteudo);
			System.out.println("não encontrado\n\n");
		}
	}
}