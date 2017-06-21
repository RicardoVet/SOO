package br.com.unesp.servicos;

import java.util.StringTokenizer;

import br.com.unesp.beans.Arquivo;

public class Springer implements Parser{

	@Override
	public String getParser(String conteudo, Arquivo arq) {
		System.out.println("sem virgula e com chaves");
		String[] result = conteudo.split("\\{", 2);
		arq.setTipo(result[0]);
		String[] result2 = result[1].split(",", 2);

		String key = result2[0];


		StringBuilder builder = new StringBuilder(result2[1]);
		builder.replace(result2[1].lastIndexOf("}"), result2[1].lastIndexOf("}")+1, "");
		StringTokenizer tokens = new StringTokenizer(builder.toString(), "{|}");
		String chave = null;
		String valor = null;
		while(tokens.hasMoreTokens()){
			if (tokens.countTokens() % 2 == 0){
				valor = tokens.nextToken();
				System.out.println(chave+"-->"+valor);
				arq.getAtributos().put(chave, valor);
			}
			else
				chave = tokens.nextToken();
		}
		return null;
	}
}
