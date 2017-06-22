package br.com.unesp.servicos;

import java.util.StringTokenizer;

import br.com.unesp.beans.Arquivo;

public class Science2 implements Parser{

	@Override
	public String getParser(String conteudo, Arquivo arq) {
		System.out.println("com aspas e sem virgula");
		String[] result = conteudo.split("\\{", 2);
		arq.setTipo(result[0]);
		String[] result2 = result[1].split(",", 2);

		arq.setReferencias(result2[0]);

		result2[1] = result2[1].replace("\"\"", "\" \"");
		StringTokenizer tokens = new StringTokenizer(result2[1], "\"");
		String chave = null;
		String valor = null;
		while(tokens.hasMoreTokens()){
			if (tokens.countTokens() % 2 == 0){
				valor = tokens.nextToken();
				arq.getAtributos().put(chave, valor);
			}
			else{
				chave = tokens.nextToken();
				chave = chave.replace(",", "").replace("=", "").trim();
			}
		}
		return null;
	}
}
