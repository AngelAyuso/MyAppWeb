package com.myappweb.mvc;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import services.UsuarioServ;

@Controller
public class UsuarioController {

	private static final Logger logger = Logger.getLogger(UsuarioServ.class);
	
	@Autowired
	private UsuarioModel usuarioMod; 
	
	/**
	 * Controller de la pantalla principal
	 * http://localhost:8080/index
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/index")
	public String index (Model model) {
		return "index";
	}
	
	@RequestMapping(path="/logout")
	public String logout (Model model) {
		return "index";
	}
	
	@RequestMapping(path="/altaUsuario")
	public String altaUsuario (Model model) {
		UsuarioCA usuarioCA = new UsuarioCA();
		model.addAttribute("usuarioCA", usuarioCA);
		return "altaUsuario";
	}
	
	@RequestMapping(path="/perfil")
	public String getPerfilUser (Model model, String idUsuario) {
		CAResultadoOUT resultadoOUT = usuarioMod.getUsuarioById(idUsuario);
		UsuarioCA usuario = new UsuarioCA();
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = resultadoOUT.getUsuario();
		}
		model.addAttribute("usuarioCA", usuario);
		return "perfil";
	}
	
	@RequestMapping(path="/login")
	public String login (Model model, String user, String pwd) {
		
		CAResultadoOUT resultadoOUT = usuarioMod.login(user, pwd);
		UsuarioCA usuario = new UsuarioCA();
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = resultadoOUT.getUsuario();
		
			if(pwd.equals(usuario.getPassword())) {
				model.addAttribute("login", Boolean.valueOf(true));
				model.addAttribute("idUsuario", usuario.getIdUsuario());
				model.addAttribute("nombreUsuario", usuario.getNombre());
				return "menu";
			//Error Contrasena
			} else {
				model.addAttribute("login", Boolean.valueOf(false));
				model.addAttribute("errorLogin", Boolean.valueOf(true));
				model.addAttribute("errorMsg", "Contraseña incorrecta");
				return "index";
			}
		//Error Usuario no encontrado
		} else {
			model.addAttribute("login", Boolean.valueOf(false));
			model.addAttribute("errorLogin", Boolean.valueOf(true));
			model.addAttribute("errorMsg", "Usuario no encontrado");
			return "index";
		}
	}
	
	
	@RequestMapping(path="/editarPerfil", method = RequestMethod.POST)
	public String editarPerfil (@Valid @ModelAttribute("usuarioCA") UsuarioCA usuarioForm, BindingResult result, Model model) {
		
		logger.info("/editarPerfil ");
		logger.info("Parametros entrada: ");
		
		 if (result.hasErrors()) {			
	        return "perfil";
	    }
		
		UsuarioCA usuario = new UsuarioCA();
		
		CAResultadoOUT resultadoOUT = usuarioMod.editarUsuario(usuarioForm);
		if(Constantes.CONS_RESULTADO_OK.equals(resultadoOUT.getResultado())) {
			usuario = new UsuarioCA();
			resultadoOUT = usuarioMod.getUsuarioById(Integer.toString(usuarioForm.getIdUsuario()));
		}
		
		model.addAttribute("usuario", usuario);
		
		return "perfil";
	}
	
	@RequestMapping(path="/altaUsuarioForm", method = RequestMethod.POST)
	public String altaUsuarioForm (@Valid @ModelAttribute("usuarioCA") UsuarioCA usuario, BindingResult result, String password2, Model model) {
		
		logger.info("/altaUsuarioForm ");
		logger.info("Parametros entrada: ");
		
		 if (result.hasErrors()) {			
	        return "altaUsuario";
	    }
		
		CAResultadoOUT beanResultado = usuarioMod.altaUsuarioForm(usuario); 
		 
		if(Constantes.CONS_RESULTADO_OK.equals(beanResultado.getResultado())) {	
			model.addAttribute("login", Boolean.valueOf(true));
			model.addAttribute("usuario", usuario);
			return "menu";
		} else {
			return "altaUsuario";
		}
	}
}