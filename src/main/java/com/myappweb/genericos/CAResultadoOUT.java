package com.myappweb.genericos;

public class CAResultadoOUT {
	
	private String resultado;
	
	private String codError;
	
	private UsuarioCA usuario;

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getCodError() {
		return codError;
	}

	public void setCodError(String codError) {
		this.codError = codError;
	}

	public UsuarioCA getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioCA usuario) {
		this.usuario = usuario;
	}	
}
