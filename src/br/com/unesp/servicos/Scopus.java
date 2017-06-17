package br.com.unesp.servicos;

import java.util.StringTokenizer;

import br.com.unesp.beans.Arquivo;

public class Scopus implements Parser {

	@Override
	public String getParser(String conteudo, Arquivo arq) {
		if (conteudo.contains("=")) {
			StringTokenizer token = new StringTokenizer(conteudo, "=");
			StringBuilder builder = new StringBuilder();
			String str = "";
			String str2 = "";

			if (token.hasMoreTokens()) {
				builder.append(token.nextToken());
				str = builder.toString().substring(builder.lastIndexOf(",") + 1, builder.length());
				str2 = builder.toString().substring(builder.indexOf("@"), builder.lastIndexOf(","));
				builder.replace(0, builder.length(), "");
			}

			StringTokenizer ref = new StringTokenizer(str2, "{");
			if (ref.hasMoreTokens())
				arq.setTipo(ref.nextToken().replace("@", "").trim());

			ref = new StringTokenizer(str2, ",");
			while (ref.hasMoreElements())
				arq.getReferencias().add(ref.nextToken().replace("@" + arq.getTipo(), "").replace("{", ""));

			while (token.hasMoreTokens())
				str += token.nextToken();
			if (str.contains("@")) {
				int inicio = str.indexOf("@");
				conteudo = "";
				conteudo = str.substring(inicio);
				StringBuilder temp = new StringBuilder();
				temp.append(str);
				temp.replace(inicio, str.length(), "");
				str = "";
				str = temp.toString();
			} else
				conteudo = "";

			token = new StringTokenizer(str, ",");
			str = "";
			while (token.hasMoreTokens())
				str += token.nextToken();

			token = new StringTokenizer(str, "{|}");
			if ((token.countTokens() % 2) == 0) {
				while (token.hasMoreTokens())
					arq.getAtributos().put(token.nextToken().trim(), token.nextToken().trim());
			}
		} else {
			String str = conteudo;
			if (str.contains("@")) {
				int inicio = str.indexOf("@");
				int fim = str.indexOf("@", inicio + 1) == -1 ? str.length() : str.indexOf("@", inicio + 1);
				conteudo = "";
				conteudo = str.substring(fim);
				StringBuilder temp = new StringBuilder();
				temp.append(str);
				temp.replace(fim, str.length(), "");
				str = "";
				str = temp.toString();
			} else
				conteudo = "";

			String str2 = str.substring(0, str.indexOf(",", str.lastIndexOf(":")));
			StringBuilder builder = new StringBuilder();
			builder.append(str);
			builder.replace(0, str2.length() + 1, "");
			str = "";
			str = builder.toString();

			StringTokenizer ref = new StringTokenizer(str2, "{");
			if (ref.hasMoreTokens())
				arq.setTipo(ref.nextToken().replace("@", "").trim());

			ref = new StringTokenizer(str2, ",");
			while (ref.hasMoreElements())
				arq.getReferencias().add(ref.nextToken().replace("@" + arq.getTipo(), "").replace("{", ""));

			StringTokenizer token = new StringTokenizer(str, ",");
			str = "";
			while (token.hasMoreTokens())
				str += token.nextToken();

			token = new StringTokenizer(str, "{|}");
			if ((token.countTokens() % 2) == 0) {
				while (token.hasMoreTokens())
					arq.getAtributos().put(token.nextToken().trim(), token.nextToken().trim());
			}
		}
		return conteudo;
	}
}
