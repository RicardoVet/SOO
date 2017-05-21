package br.com.unesp.beans;

import java.io.IOException;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import br.com.unesp.constatntes.Const;
import br.com.unesp.negocios.LeitorArquivos;

public class Bib extends Arquivo {

	public Bib() {
		setReferencias(new ArrayList<>());
	}

	private String lerArquivo(int indice, String nome) {
		LeitorArquivos leitor = new LeitorArquivos();
		try {

			return leitor.lerArquivo(FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath(Const.path.getValue() + nome));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean completeBean(String conteudo) {
		String partes[] = conteudo.trim().split("author");
		String labels[] = partes[1].trim().split(",");
		int cont = 0;
		for (String valores : labels) {
			String values[] = valores.trim().split("=");
			for (String valor : values) {
				String res = valor.replace("{", "").replace("}", "").trim();
				if (cont % 2 != 0) {
					switch (cont) {
					case 1:
						this.setAuthor(res);
						break;
					case 3:
						this.setTitle(res);
						break;
					case 5:
						this.setJournal(res);
						break;
					case 7:
						this.setYear(res);
						break;
					case 9:
						this.setVolume(res);
						break;
					case 11:
						this.setNumber(res);
						break;
					case 13:
						this.setPages(res);
						break;

					default:
						break;
					}
				}
				cont++;
			}
		}
		return true;
	}

	@Override
	public void parser(int indice, String nome) {
		completeBean(lerArquivo(indice, nome));
	}
}
