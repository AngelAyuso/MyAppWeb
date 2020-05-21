package com.myappweb.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myappweb.genericos.CAResultadoOUT;
import com.myappweb.genericos.UsuarioCA;
import com.myappweb.utilidades.Constantes;
import com.myappweb.utilidades.UtilGenericos;

import services.UsuarioServ;
import utils.Data;
import generics.ServResultadoOUT;

@Component
public class UsuarioModel {
	
	private static final Logger logger = Logger.getLogger(UsuarioServ.class);
	
	@Autowired
	UtilGenericos utilGenericos;
	
	private UsuarioServ usuarioServ = new UsuarioServ();
	
	/**
	 * Metodo de Login - Devuelve un Usuario por NIF/EMAIL y Password
	 * @param user
	 * @param pwd
	 * @return
	 */
	public CAResultadoOUT login(String user, String pwd) {
		
		logger.info("UsuarioModel.login - INICIO");
		logger.info("UsuarioModel.login -   Parametros de entrada:");
		logger.info("UsuarioModel.login -     user - " + user);
		logger.info("UsuarioModel.login -     pwd - " + pwd);
		
		 CAResultadoOUT resultadoCABean = new CAResultadoOUT();
		 if(Data.isValid(user) && Data.isValid(pwd)) {
		
		      // Comprueba formato DNI
		      Pattern p = Pattern.compile("[0-9]{7,8}[A-Za-z]");
		      Matcher m = p.matcher(user);
		      
		      ServResultadoOUT resultadoSRVBean;
		     
		      if (m.find()) {
		    	  resultadoSRVBean = usuarioServ.getUsuarioByNifOrEmail(user, null);
		      } else {
		    	  resultadoSRVBean = usuarioServ.getUsuarioByNifOrEmail(null, user);
		      }
		      
		      if(Constantes.CONS_RESULTADO_OK.equals(resultadoSRVBean.getResultado())) {
		    	  resultadoCABean.setUsuario(utilGenericos.convertUserBDtoModel(resultadoSRVBean.getUsuario()));
		    	  resultadoCABean.setResultado(Constantes.CONS_RESULTADO_OK);
		      } else {
		    	  resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
		    	  resultadoCABean.setCodError(resultadoSRVBean.getCodError());
		      }
		 } else {
			 resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
		 }
	      
	      logger.info("UsuarioModel.login - FIN");
	      
	      return resultadoCABean;
	}
	
	/**
	 * Metodo que devuelve un Usuario por idUsuario
	 * @param idUsuario
	 * @return Usuario
	 */
	public CAResultadoOUT getUsuarioById(String idUsuario) {
		
		logger.info("UsuarioModel.getUsarioById - INICIO");
		logger.info("UsuarioModel.getUsarioById -   Parametros de entrada:");
		logger.info("UsuarioModel.getUsarioById -     idUsuario - " + idUsuario);
		CAResultadoOUT resultadoCABean = new CAResultadoOUT();
		ServResultadoOUT resultadoSRVBean = usuarioServ.getUsuarioById(Integer.parseInt(idUsuario));
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoSRVBean.getResultado())) {
			resultadoCABean.setResultado(Constantes.CONS_RESULTADO_OK);
			resultadoCABean.setUsuario(utilGenericos.convertUserBDtoModel(resultadoSRVBean.getUsuario()));
		} else {
			resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
		}
		logger.info("UsuarioModel.getUsuarioById - FIN");
		
		return resultadoCABean;
	}
	
	/**
	 * Metodo que edita el perfil de un Usuario
	 * @return
	 */
	public CAResultadoOUT editarUsuario(UsuarioCA usuarioCA) {
		
		logger.info("UsuarioModel.editarUsuario - INICIO");
		logger.info("UsuarioModel.editarUsuario -   Parametros de entrada:");
		logger.info("UsuarioModel.editarUsuario -     Usuario - " + usuarioCA);
		CAResultadoOUT resultadoCABean = new CAResultadoOUT();
		ServResultadoOUT resultadoSRVBean = usuarioServ.updateUsuario(utilGenericos.convertUserModeltoBD(usuarioCA));
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoSRVBean.getResultado())) {
			resultadoCABean.setResultado(Constantes.CONS_RESULTADO_OK);
		} else {
			resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
		}
		logger.info("UsuarioModel.editarUsuario - FIN");
		
		return resultadoCABean;
	}
	
	/**
	 * Metodo que da de alta el perfil de un Usuario
	 * @return
	 */
	public CAResultadoOUT altaUsuarioForm(UsuarioCA usuario) {
		
		logger.info("UsuarioModel.altaUsuarioForm - INICIO");
		
		UsuarioCA usuarioAux = null;
		Boolean existeUsuario = Boolean.valueOf(false);
		CAResultadoOUT resultadoCABean = new CAResultadoOUT();
		
		//Se comprueba si existe usurio por DNI
		ServResultadoOUT resultadoSRVBean = usuarioServ.getUsuarioByNifOrEmail(usuario.getDni(), null);
		if(Constantes.CONS_RESULTADO_KO.equals(resultadoSRVBean.getResultado())) {
			
			if(resultadoSRVBean.getUsuario() != null) {
				existeUsuario = Boolean.valueOf(true);
				resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
				logger.info("UsuarioModel.altaUsuarioForm -   Existe usuario con DNI " + usuarioAux.getDni());
			//Se comprueba si existe usuario por EMAIL
			} else {
				resultadoSRVBean = usuarioServ.getUsuarioByNifOrEmail(null, usuario.getEmail());
				if(Constantes.CONS_RESULTADO_KO.equals(resultadoSRVBean.getResultado())) {
					if(resultadoSRVBean.getUsuario() != null) {
						existeUsuario = Boolean.valueOf(true);
						resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
						logger.info("UsuarioModel.altaUsuarioForm -   Existe usuario con Email " + usuarioAux.getEmail());
					}
				} else {
					resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
				}
			}
			
		} else {
			resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
		}
		
		if(!existeUsuario) {
			logger.info("UsuarioModel.altaUsuarioForm -   Se invoca usuarioServ.insertUsuario");
			resultadoSRVBean = usuarioServ.insertUsuario(utilGenericos.convertUserModeltoBD(usuario));
			if(Constantes.CONS_RESULTADO_OK.equals(resultadoSRVBean.getResultado())) {
				resultadoCABean.setResultado(Constantes.CONS_RESULTADO_OK);
				//Se devuelve el usuaro creado buscandolo por el NIF
				resultadoSRVBean = usuarioServ.getUsuarioByNifOrEmail(usuario.getDni(), null);
				if(Constantes.CONS_RESULTADO_OK.equals(resultadoSRVBean.getResultado())) {
					resultadoCABean.setUsuario(utilGenericos.convertUserBDtoModel(resultadoSRVBean.getUsuario()));
				}
			} else {
				resultadoCABean.setResultado(Constantes.CONS_RESULTADO_KO);
			}
		}		
		return resultadoCABean;
	}
}