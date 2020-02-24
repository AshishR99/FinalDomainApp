package net.codejava.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import net.codejava.entities.Content;
import net.codejava.entities.Domain;
import net.codejava.entities.Subdomain;
import net.codejava.payload.DomainSubdomainPayload;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.SubdomainService;


@RestController
@CrossOrigin("*")
public class SubdomainController {

	boolean status = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(UserController.class);

	@Autowired
	private DomainService domainService;

	@Autowired
	private SubdomainService subdomainService;
	
	@Autowired
	private ContentService contentService;
	
	
	//----------------------------------------Sub Domain portion-----------------------------------------------//
	//Sub-domain handler methods

	//Get subdomains details using domain id:   testing purpose...... not used ....
	@RequestMapping("/api/subdomain/getSubDomainByDomainId/{fk_domain_id}")
	public ResponseEntity<?> searchSubDomainUsingDomainId(@PathVariable(name = "fk_domain_id")int domain_id)
	{
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
		Subdomain filteredSubdomain = null;


		List<Subdomain> getAllSubDomains = subdomainService.listAll();
		List<Subdomain> subdomainsUnderParticularDomain = new ArrayList<Subdomain>();
		for(Subdomain subdomain: getAllSubDomains)
		{
			if(subdomain.getFK_domain_id()==domain_id)
			{
				filteredSubdomain = subdomain;
				subdomainsUnderParticularDomain.add(filteredSubdomain);

			}

		}

		if(subdomainsUnderParticularDomain.isEmpty())
		{
			System.out.println("No such subdomain exists under this domain id");
			status = false;
			statusMessage = "No such subdomain exists with this domain id!!!";
			
//			mav.addObject("subdomainsUnderParticularDomain",subdomainsUnderParticularDomain);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("subdomainsUnderParticularDomain", subdomainsUnderParticularDomain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}
		
		else
		{
			status = true;
			statusMessage = "Retrieving Subdomain under your domain successfully!!!";

//			mav.addObject("subdomainsUnderParticularDomain",subdomainsUnderParticularDomain);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("subdomainsUnderParticularDomain", subdomainsUnderParticularDomain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}


//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
		
	}





	// Save Domain details
	@RequestMapping(value = "/api/subdomain/save_subdomain", method = RequestMethod.POST)
	public ResponseEntity<?> saveSubDomain(@RequestBody Subdomain subdomain) 
	{

		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();

		int fk_domain_id = subdomain.getFK_domain_id();
		//String fk_domain_name = subdomain.getFK_domain_name();
		String subdomain_name= subdomain.getSubdomainName();

		System.out.println("Foreign key Domain"+fk_domain_id);
		List<Subdomain> listOfSubdomains = subdomainService.loadSubDomainsByDomainId(fk_domain_id);
		boolean isDuplicateSubdomainEntry = checkDuplicateSubdomainEntry(listOfSubdomains,subdomain_name);

		if(isDuplicateSubdomainEntry)
		{
			System.out.println("This Subdomain is already exists ..... plz enter any other Subdomain");
			status = false;
			statusMessage = "This Subdomain is already exists ..... plz enter any other Subdomain";

//			mav.addObject("subdomain", subdomain);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("subdomain", subdomain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

		else
		{
			status = subdomainService.save(subdomain);
			if(status)
			{
				status = true;
				statusMessage="Subdomain Saved successfully!!!";

//				mav.addObject("subdomain", subdomain);
//				mav.addObject("statusMessage", statusMessage);
//				mav.addObject("status", status);
				
				data.put("subdomain", subdomain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

			else
			{
				status = false;
				statusMessage ="Subdomain not saved...something went wrong!!!";

//				mav.addObject("subdomain", subdomain);
//				mav.addObject("statusMessage", statusMessage);
//				mav.addObject("status", status);
				
				data.put("subdomain", subdomain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}

	
	

	// Update Domain details
	@RequestMapping(value = "/api/subdomain/update_subdomain", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSubDomain(@RequestBody Subdomain subdomain) 
	{

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();

		int fk_domain_id = subdomain.getFK_domain_id();
	//	String fk_domain_name = subdomain.getFK_domain_name();
		String subdomain_name= subdomain.getSubdomainName();

		System.out.println("Foreign key Domain"+fk_domain_id);
		List<Subdomain>listOfSubdomains = subdomainService.loadSubDomainsByDomainId(fk_domain_id);
		boolean isDuplicateSubdomainEntry = checkDuplicateSubdomainEntry(listOfSubdomains,subdomain_name);

		if(isDuplicateSubdomainEntry)
		{
			System.out.println("This Subdomain is already exists against other id ..... plz enter any other Subdomain");
			status = false;
			statusMessage = "This Subdomain is already exists ..... plz enter any other Subdomain";

//			mav.addObject("subdomain", subdomain);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("subdomain", subdomain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

		else
		{
			status = subdomainService.save(subdomain);
			if(status)
			{
				status = true;
				statusMessage="Subdomain updated successfully!!!";

//				mav.addObject("subdomain", subdomain);
//				mav.addObject("statusMessage", statusMessage);
//				mav.addObject("status", status);
				
				data.put("subdomain", subdomain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

			else
			{
				status = false;
				statusMessage ="Subdomain not updated...something went wrong!!!";

//				mav.addObject("subdomain", subdomain);
//				mav.addObject("statusMessage", statusMessage);
//				mav.addObject("status", status);
				
				data.put("subdomain", subdomain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}



	//Check Sub Domain Dupliacte Entry
	public boolean checkDuplicateSubdomainEntry(List<Subdomain> listOfSubdomains, String subdomain_name)
	{
		boolean isDuplicate=false;
		for(int j=0;j<listOfSubdomains.size();j++)
		{
			Subdomain subdomainObj = (Subdomain)listOfSubdomains.get(j);				
			if(subdomain_name.compareToIgnoreCase(subdomainObj.getSubdomainName())==0)
			{
				isDuplicate = true;
			}


		}

		return isDuplicate;

	}

	// Get All SubDomains
	@RequestMapping("/api/subdomain/getSubdomains")
	public ResponseEntity<?> getAllSubDomains()
	{
		Map<String, Object> data = new HashMap<String,Object>();
		
		List<DomainSubdomainPayload> listOfSubdomains = subdomainService.listSubdomainsWithDomainNames();

		if(!listOfSubdomains.isEmpty())
		{
			status = true;
			statusMessage = "Retrieving subdomains.....!!!";
		}
		else 
		{
			status = false;
			statusMessage = "List of Subdomains is empty!!!";

		}

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
//		mav.addObject("listOfSubdomains", listOfSubdomains);
//		mav.addObject("status", status);
//		mav.addObject("statusMessage", statusMessage);
		
		data.put("listOfSubdomains", listOfSubdomains);
		data.put("status", status);
		data.put("statusMessage", statusMessage);
		
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}

	
	
	//Edit Sub Domain Details
	@RequestMapping("/api/subdomain/getBySubdomainId/{subdomainId}")
	public ResponseEntity<?> showEditSubDomainPage(@PathVariable(name = "subdomainId") int subdomainId) 
	{
		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			Subdomain subdomain = subdomainService.get(subdomainId);
			status = true;
			statusMessage = "Subdomain Data has been fetched successfully..";

//			mav.addObject("subdomain", subdomain);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("subdomain", subdomain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}
		
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Subdomain not found..something went wrong!!!";

//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}
		
		

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}

	//Delete Sub Domain details
	@RequestMapping(value = "/api/subdomain/delete_subdomain/{subdomainId}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSubDomain(@PathVariable(name = "subdomainId") int subdomainId) 
	{
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			
			List<Content> listOfContentsBySubdomainID = contentService.findContentBySubdomainId(subdomainId);
			if(listOfContentsBySubdomainID.isEmpty())
			{

				subdomainService.delete(subdomainId);
				status = true;
				statusMessage = "Subdomain has been deleted successfully!!!";

//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);

				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

			else
			{
				status = false;
				statusMessage = "This Subdomain is having content...delete content first to remove this subdomain";

//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);


			}
		}
		
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something is wrong...Deletion of subdomain can't be done...try again later!!!";

//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

		
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}

	

}
