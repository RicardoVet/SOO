package br.com.unesp.beans;

import java.util.List;

public abstract class Arquivo {

	private List<String> referencias;
	private String author;
	private String title;
	private String journal;
	private String year;
	private String volume;
	private String number;
	private String pages;
	
	public abstract void parser(int indice, String nome);
	
	public List<String> getReferencias() {
		return referencias;
	}
	public void setReferencias(List<String> referencias) {
		this.referencias = referencias;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
}
