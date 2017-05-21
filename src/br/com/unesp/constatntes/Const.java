package br.com.unesp.constatntes;

public enum Const {

	path("/WEB-INF/upload/");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setLocal(String value) {
		this.value = value;
	}

	private Const(String value) {
		this.value = value;
	}

}
