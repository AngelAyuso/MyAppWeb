package com.myappweb.utilidades;

import org.springframework.stereotype.Component;

import com.myappweb.genericos.UsuarioCA;

@Component
public class UtilGenericos {
	
	
	public UsuarioCA convertUserBDtoModel(entidades.Usuario usuarioBD) {
		
		System.out.println("MyAppWeb - UtilGenericos.convertUserBDtoModel - INICIO");
		
		UsuarioCA usuarioModel = null;
		if(usuarioBD != null) {
			usuarioModel = new UsuarioCA();
			usuarioModel.setIdUsuario(usuarioBD.getIdUsuario());
			usuarioModel.setDni(usuarioBD.getDni());
			usuarioModel.setEmail(usuarioBD.getEmail());
			usuarioModel.setNombre(usuarioBD.getNombre());
			usuarioModel.setPassword(usuarioBD.getPassword());
			usuarioModel.setPrimerApellido(usuarioBD.getPrimerApellido());
			usuarioModel.setSegundoApellido(usuarioBD.getSegundoApellido());
			usuarioModel.setTelefono(usuarioBD.getTelefono());
		}
		System.out.println("MyAppWeb - UtilGenericos.convertUserBDtoModel - FIN");
		return usuarioModel;
	}
	
	public entidades.Usuario convertUserModeltoBD(UsuarioCA usuarioModel) {
		
		System.out.println("MyAppWeb - UtilGenericos.convertUserModeltoBD - INICIO");
		
		entidades.Usuario usuarioBD = null;
		if(usuarioModel != null) {
			usuarioBD = new entidades.Usuario();
			usuarioBD.setIdUsuario(usuarioModel.getIdUsuario());
			usuarioBD.setDni(usuarioModel.getDni());
			usuarioBD.setEmail(usuarioModel.getEmail());
			usuarioBD.setNombre(usuarioModel.getNombre());
			usuarioBD.setPassword(usuarioModel.getPassword());
			usuarioBD.setPrimerApellido(usuarioModel.getPrimerApellido());
			usuarioBD.setSegundoApellido(usuarioModel.getSegundoApellido());
			usuarioBD.setTelefono(usuarioModel.getTelefono() != null ? usuarioModel.getTelefono().intValue() : null);
		}
		System.out.println("MyAppWeb - UtilGenericos.convertUserModeltoBD - FIN");
		return usuarioBD;
	}
	
}