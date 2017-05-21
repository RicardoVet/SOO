package br.com.unesp.negocios;

import java.io.FileOutputStream;
import java.io.IOException;

public class UploadArquivo {

	public void salvar(byte[] dados, String nomeArquivo, String arquivo) throws IOException {
		FileOutputStream fout = new FileOutputStream(arquivo); {
        fout.write(dados);
        fout.flush();
        fout.close();
		}
	}
}
