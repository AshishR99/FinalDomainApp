package net.codejava.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.codejava.entities.Role;
import net.codejava.entities.Userlogin;
import net.codejava.services.RoleService;
import net.codejava.services.UserloginService;

@RestController
@CrossOrigin("*")
public class RoleController {

	boolean status = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserloginService userloginService;


	//	@ModelAttribute
	//	public void setVaryResponseHeader(HttpServletResponse response) {
	//		//response.setHeader("Access-Control-Allow-Origin", "false");
	//		//response.setHeader("Access-Control-Allow-Origin","http://192.168.0.173:8080/new_domain_page");
	//		response.setHeader("Access-Control-Allow-Origin", "*");
	//		response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, PATCH");
	//		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	//		response.setHeader("Access-Control-Allow-Credentials","true");
	//	} 
	//




	//---------------------------------------------Role Portion--------------------------------------
	// Role Handler Methods
	
	
	//List of Roles
		@RequestMapping(value = "/api/role/getRoles")
		public ResponseEntity<?> allRoles()
		{

			Map<String, Object> data = new HashMap<String,Object>();
			
			
			try
			{
				List<Role> listOfRoles = roleService.listAll();
				if(listOfRoles.size()>0)
				{
					status = true;
					statusMessage = "List of roles fetched successfully!!!";
					
					data.put("listOfRoles", listOfRoles);
					data.put("status", status);
					data.put("statusMessage", statusMessage);

				}
				else
				{
					status = false;
					statusMessage = "List of Roles is empty!!!";
					
					data.put("listOfRoles", listOfRoles);
					data.put("status", status);
					data.put("statusMessage", statusMessage);

				}
				
				
			}
			
			catch(Exception ex)
			{
				List<Role> listOfRoles = null;
				status = false;
				statusMessage = "List of Roles can't be fetched...something went wrong!!!";
				
				
				data.put("listOfRoles", listOfRoles);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				
			}

			return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);

		}




	// Save Role details
	@RequestMapping(value = "/api/role/save_role", method = RequestMethod.POST)
	public ResponseEntity<?> saveRole(@RequestBody Role role) 
	{

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
		
		String role_desc = role.getRole_desc();
		System.out.println("Role Description:"+role_desc);
		List<Role> listOfRoles = roleService.listAll();
		boolean isDuplicateRoleExists = checkDuplicateRoleEntry(listOfRoles,role_desc);
		if(isDuplicateRoleExists)
		{
			System.out.println("This Role is already exists ..... plz enter any other role");
			status = false;
			statusMessage = "This role is already exists ..... plz enter any other role";

			
			data.put("role", role);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}

		else
		{

			status = roleService.save(role);
			if(status)
			{
				status = true;
				statusMessage = "Role Saved successfully!!!";

				
				data.put("role", role);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			else
			{
				status = false;
				statusMessage = "Something went wrong...plz try again later!!!";

				
				data.put("role", role);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}


		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}


	
	// Update Role details
	@RequestMapping(value = "/api/role/update_role", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateRole(@RequestBody Role role) 
	{

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		

		String role_desc = role.getRole_desc();
		System.out.println("Role Description:"+role_desc);
		List<Role> listOfRoles = roleService.listAll();
		boolean isDuplicateRoleExists = checkDuplicateRoleEntry(listOfRoles,role_desc);
		if(isDuplicateRoleExists)
		{
			System.out.println("This Role is already exists for other id..... plz enter any other role");
			status = false;
			statusMessage = "This role is already exists for other id...... plz enter any other role";

//			mav.addObject("role", role);
//			mav.addObject("status", status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("role", role);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}

		else
		{

			status = roleService.save(role);
			if(status)
			{
				status = true;
				statusMessage = "Role Updated successfully!!!";

//				mav.addObject("role", role);
//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("role", role);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			else
			{
		//		status = false;
				statusMessage = "Role not updated...Something went wrong...plz try again later!!!";

//				mav.addObject("role", role);
//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("role", role);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}

	
	
	// Check Domain Dupliacte Entry
	public boolean checkDuplicateRoleEntry(List<Role> listOfRoles, String role_desc)
	{
		boolean isDuplicate=false;
		for(int j=0;j<listOfRoles.size();j++)
		{

			Role roleobj = (Role) listOfRoles.get(j);
			if(role_desc.compareToIgnoreCase(roleobj.getRole_desc())==0)
			{
				isDuplicate = true;
			}

		}

		return isDuplicate;

	}




	//Edit Role Details
	@RequestMapping("/api/role/getByRoleId/{role_id}")
	public ResponseEntity<?> showEditRolePage(@PathVariable(name = "role_id") int role_id) 
	{

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
		try
		{
			Role role = roleService.get(role_id);
			status = true;
			statusMessage = "Role Data has been fetched successfully..";

//			mav.addObject("role", role);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("role", role);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			
			

		}

		catch(Exception ex)
		{
			status = false;
			statusMessage = "Role not found..something went wrong!!!";

//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
//			data.put("role", role);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}


//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}


	//Delete Role details
	@RequestMapping(value = "/api/role/delete_role/{role_id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRole(@PathVariable(name = "role_id") int role_id) 
	{
		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
		try
		{
			List<Userlogin> userloginList = userloginService.findByRoleId(role_id);
			if(userloginList.isEmpty()==true)
			{
				roleService.delete(role_id);
				status = true;
				statusMessage = "Role has been deleted successfully!!!";

//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);

				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}

			else
			{
				status = false;
				statusMessage = "This role is having user...delete user first to remove this role";
//
//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				

				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}

		}

		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something is wrong...Deletion can't be done...try again later!!!";

//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

		//	mav.addObject("userloginList", userloginList);
//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
		
	}


}
