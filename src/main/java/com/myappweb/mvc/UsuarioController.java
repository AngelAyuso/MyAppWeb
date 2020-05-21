package com.myappweb.mvc;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myappweb.genericos.CAResultadoOUT;
import com.myappweb.genericos.UsuarioCA;
import com.myappweb.model.UsuarioModel;
import com.myappweb.utilidades.Constantes;
import com.myappweb.utilidades.Utils;

import services.UsuarioServ;

@Controller
public class UsuarioController {

	private static final Logger logger = Logger.getLogger(UsuarioServ.class);
	
	@Autowired
	private UsuarioModel usuarioMod;
	
	@Autowired
	private MessageSource mensajes;
	
	/**
	 * Controller de la pantalla principal
	 * http://localhost:8080/index
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/index")
	public String index (Model model) {
		logger.info("/index ");
		return Constantes.VIEW_INDEX;
	}
	
	/**
	 * Controller para cerrar sesion de la aplicacion
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/logout")
	public String logout (Model model) {
		logger.info("/logout ");
		return Constantes.VIEW_INDEX;
	}
	
	/**
	 * Controller para acceder a la vista de Alta de Usuario
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/altaUsuario")
	public String altaUsuario (Model model) {
		logger.info("/altaUsuario ");
		UsuarioCA usuarioCA = new UsuarioCA();
		model.addAttribute("usuarioCA", usuarioCA);
		return Constantes.VIEW_ALTA_USUARIO;
	}
	
	/**
	 * Controller para acceder a la vista de Actualizar Usuario
	 * @param model
	 * @param idUsuario
	 * @return
	 */
	@RequestMapping(path="/perfil")
	public String getPerfilUser (Model model, String idUsuario) {
		logger.info("/perfil ");
		
		CAResultadoOUT resultadoOUT = usuarioMod.getUsuarioById(idUsuario);
		UsuarioCA usuario = new UsuarioCA();
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = resultadoOUT.getUsuario();
		}
		model.addAttribute("usuarioCA", usuario);
		return Constantes.VIEW_MODIFICAR_USUARIO;
	}
	
	/**
	 * Controller que accede al menu de la aplicacion
	 * @param model
	 * @param idUsuario
	 * @return
	 */
	@RequestMapping(path="/menu")
	public String menu (Model model, String idUsuario) {
		logger.info("/model ");
		
		CAResultadoOUT resultadoOUT = usuarioMod.getUsuarioById(idUsuario);
		UsuarioCA usuario = new UsuarioCA();
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = resultadoOUT.getUsuario();
		}
		
		model.addAttribute("login", Boolean.valueOf(true));
		model.addAttribute("idUsuario", usuario.getIdUsuario());
		model.addAttribute("nombreUsuario", usuario.getNombre());
		return Constantes.VIEW_MENU;
	}
	
	/**
	 * Controller del login
	 * @param model
	 * @param user
	 * @param pwd
	 * @return
	 */
	@RequestMapping(path="/login")
	public String login (Model model, String user, String pwd) {
		logger.info("/login ");

		CAResultadoOUT resultadoOUT = usuarioMod.login(user, pwd);
		UsuarioCA usuario = new UsuarioCA();
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = resultadoOUT.getUsuario();
		
			if(pwd.equals(usuario.getPassword())) {
				model.addAttribute("login", Boolean.valueOf(true));
				model.addAttribute("idUsuario", usuario.getIdUsuario());
				model.addAttribute("nombreUsuario", usuario.getNombre());
				return Constantes.VIEW_MENU;
			//Error Contrasena
			} else {
				model.addAttribute("login", Boolean.valueOf(false));
				model.addAttribute("errorLogin", Boolean.valueOf(true));
				model.addAttribute("errorMsg", mensajes.getMessage(Utils.getCodErrorLiteral(Constantes.COD_ERROR_200), null, LocaleContextHolder.getLocale()));
				return Constantes.VIEW_INDEX;
			}
		//Error Usuario no encontrado
		} else {
			model.addAttribute("login", Boolean.valueOf(false));
			model.addAttribute("errorLogin", Boolean.valueOf(true));
			model.addAttribute("errorMsg", mensajes.getMessage(Utils.getCodErrorLiteral(resultadoOUT.getCodError()), null, LocaleContextHolder.getLocale()));
			return Constantes.VIEW_INDEX;
		}
	}
	
	/**
	 * Controller que gestiona el formulario de Actualizar Usuario
	 * @param usuarioForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/updateUsuarioForm", method = RequestMethod.POST)
	public String editarPerfil (@Valid @ModelAttribute("usuarioCA") UsuarioCA usuarioForm, BindingResult result, Model model) {
		
		logger.info("/updateUsuarioForm ");
		logger.info("Parametros entrada: " + usuarioForm.toString());
		
		 if (result.hasErrors()) {			
	        return Constantes.VIEW_MODIFICAR_USUARIO;
	    }
		
		UsuarioCA usuario = new UsuarioCA();
		CAResultadoOUT resultadoOUT = usuarioMod.editarUsuario(usuarioForm);
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = new UsuarioCA();
			resultadoOUT = usuarioMod.getUsuarioById(Integer.toString(usuarioForm.getIdUsuario()));
			model.addAttribute("successUpdate", Boolean.valueOf(true));
			model.addAttribute("successMsg", mensajes.getMessage(Constantes.LITERAL_SUCCESS_UPDATE, null, LocaleContextHolder.getLocale()));
			model.addAttribute("usuario", usuario);
		} else {
			model.addAttribute("usuario", usuario);
			model.addAttribute("errorUpdate", Boolean.valueOf(true));
			model.addAttribute("errorMsg", mensajes.getMessage(Utils.getCodErrorLiteral(resultadoOUT.getCodError()), null, LocaleContextHolder.getLocale()));
		}
		return Constantes.VIEW_MODIFICAR_USUARIO;
	}
	
	/**
	 * Controller que controla el formulario del Alta de Usuario
	 * @param usuario
	 * @param result
	 * @param password2
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/altaUsuarioForm", method = RequestMethod.POST)
	public String altaUsuarioForm (@Valid @ModelAttribute("usuarioCA") UsuarioCA usuario, BindingResult result, String password2, Model model) {
		
		logger.info("/altaUsuarioForm ");
		logger.info("Parametros entrada: " + usuario.toString());
		
		 if (result.hasErrors()) {			
	        return Constantes.VIEW_ALTA_USUARIO;
	    }
		
		CAResultadoOUT resultadoOUT = usuarioMod.altaUsuarioForm(usuario);
		logger.info(" beanResultado: " + resultadoOUT.getResultado());
		 
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {	
			model.addAttribute("login", Boolean.valueOf(true));
			model.addAttribute("idUsuario", resultadoOUT.getUsuario().getIdUsuario());
			model.addAttribute("nombreUsuario", resultadoOUT.getUsuario().getNombre());
			model.addAttribute("successUpdate", Boolean.valueOf(true));
			model.addAttribute("successMsg", mensajes.getMessage(Constantes.LITERAL_SUCCESS_UPDATE, null, LocaleContextHolder.getLocale()));
			return Constantes.VIEW_MENU;
		} else {
			model.addAttribute("errorUpdate", Boolean.valueOf(true));
			model.addAttribute("errorMsg", mensajes.getMessage(Utils.getCodErrorLiteral(resultadoOUT.getCodError()), null, LocaleContextHolder.getLocale()));
			return Constantes.VIEW_MENU;
		}
	}
}