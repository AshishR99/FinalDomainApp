package net.codejava.controllers;


import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import net.codejava.entities.Content;
import net.codejava.entities.Userlogin;
import net.codejava.entities.Domain;
import net.codejava.entities.Role;
import net.codejava.entities.Subdomain;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.RoleService;
import net.codejava.services.SubdomainService;
import net.codejava.services.UserloginService;




@RestController
@CrossOrigin("*")
public class AppController 
{

	private static final Logger logger=Logger.getLogger(AppController.class);
	
	boolean status = false;
	String statusMessage = "";

	@Autowired
	private DomainService domainService;

	@Autowired
	private SubdomainService subdomainService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserloginService userloginService;


	@Autowired
	private ContentService contentService;
  


//	@ModelAttribute
//	public void setVaryResponseHeader(HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//		response.setHeader("Access-Control-Allow-Credentials","true");
//	} 



	//Admin Panel 
	@RequestMapping("/admin_panel")
	public ModelAndView viewHomePage(Model model) 
	{
	
		List<Domain> listDomains = domainService.listAll();
		List<Subdomain> listSubDomains = subdomainService.listAll();
		List<Role> listRoles = roleService.listAll();
		List<Userlogin> listUserLoginDetails = userloginService.listAll();
		List<Content> listContents = contentService.listAll();


		ModelAndView mav = new ModelAndView("index");
		//	ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		mav.addObject("listRoles",listRoles);
		mav.addObject("listDomains", listDomains);
		mav.addObject("listSubDomains", listSubDomains);
		mav.addObject("listUserLoginDetails", listUserLoginDetails);
		mav.addObject("listContents", listContents);


		return mav;


	}


	

	
}
