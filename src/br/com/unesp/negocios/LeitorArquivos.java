package br.com.unesp.negocios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivos {

	private BufferedReader buffer;
	private String conteudo;

	public String lerArquivo(String arquivo) throws IOException{
	    buffer = new BufferedReader(new FileReader(arquivo));
	    conteudo="";
		while(buffer.ready())
			conteudo+=buffer.readLine();
		buffer.close();
		return conteudo;
	}
}
