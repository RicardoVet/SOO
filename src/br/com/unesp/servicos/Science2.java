package br.com.unesp.servicos;

import java.util.StringTokenizer;

import br.com.unesp.beans.Arquivo;

/**
 * 
 * Implementação do Parser para Science
 * 
 */
public class Science2 implements Parser {

	@Override
	public String getParser(String conteudo, Arquivo arq) {
		int posicao = conteudo.lastIndexOf("\"");
		StringBuffer temp = new StringBuffer(conteudo);
		temp.replace(posicao + 1, posicao + 2, ",}");
		conteudo = "";
		conteudo = temp.toString();
		String[] result = conteudo.split("\\{", 2);
		arq.setTipo(result[0]);
		String[] result2 = result[1].split(",", 2);

		arq.setReferencias(result2[0]);

		result2[1] = result2[1].replace("\"\"", "\" \"");
		StringTokenizer tokens = new StringTokenizer(result2[1], "\"");
		String chave = null;
		String valor = null;
		while (tokens.hasMoreTokens()) {
			if (tokens.countTokens() % 2 == 0) {
				valor = tokens.nextToken();
				arq.getAtributos().put(chave, valor);
			} else {
				chave = tokens.nextToken();
				chave = chave.replace(",", "").replace("=", "").trim();
			}
		}
		return null;
	}
}
