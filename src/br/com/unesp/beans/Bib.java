package br.com.unesp.beans;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Bib extends Arquivo {

	public Bib() {
		setReferencias(new ArrayList<>());
	}

	@Override
	public void parser(String conteudo) {
		StringTokenizer token = new StringTokenizer(conteudo, "=");
		String parser ="";
		
		while (token.hasMoreTokens()) {
			parser+=(token.nextToken());
		}
		
		StringTokenizer res = new StringTokenizer(parser, ",");
		
		while(res.hasMoreTokens()){
			System.out.println(res.nextToken());
		}
	}
}
