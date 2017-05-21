package br.com.unesp.factory;

import br.com.unesp.beans.Arquivo;
import br.com.unesp.beans.Bib;

public class FileFactory {

	public static Arquivo getInstance(){
		return new Bib();
	}
}
