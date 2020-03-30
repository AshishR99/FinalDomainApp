package net.codejava.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import net.codejava.entities.Domain;
import net.codejava.entities.Userlogin;
import net.codejava.services.DomainService;
import net.codejava.services.SubdomainService;
import net.codejava.services.UserloginService;


@RestController
@CrossOrigin("*")
public class DomianController {

	//Logger log= Logger.getLogger("DomianController.class");
	boolean status = false;
	String statusMessage = "";


	@Autowired
	private DomainService domainService;

	@Autowired
	private SubdomainService subdomainService;

	@Autowired
	private UserloginService userloginService;

	private static final Logger logger=Logger.getLogger(DomianController.class);


	//	@ModelAttribute
	//	public void setVaryResponseHeader(HttpServletResponse response) {
	//		
	//		response.setHeader("Access-Control-Allow-Origin", "*");
	//		response.setHeader("Access-Control-Allow-Methods","*");
	//		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	//		response.setHeader("Access-Control-Allow-Credentials","true");
	//	} 


	//-----------------------------------------------Domain portion--------------------------------------------------
	// New Domain Page Creation
	@RequestMapping("/new_domain_page")
	public ModelAndView showNewDomainPage() 
	{
		Domain domain = new Domain();

		/*
		model.addAttribute("domain", domain);
		return new ModelAndView("new_domain.html");*/

		//ModelAndView mav = new ModelAndView("new_domain");

		ModelAndView mav = new ModelAndView("new_domain");

		//	ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		mav.addObject("domain", domain);
		return mav;

	}


	//List of domains
	@RequestMapping(value = "/api/domain/getDomains")
	public ResponseEntity<?> allDomains()
	{

		Map<String, Object> data = new HashMap<String,Object>();

		try
		{
			List<Domain> listOfDomains = domainService.listAll();
			if(listOfDomains.size()>=0)
			{
				status = true;
				statusMessage = "List is empty!!!";


				data.put("listOfDomains", listOfDomains);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			else 
			{
				status = false;
				statusMessage = "List of Domains is empty!!!";

				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
		}

		catch(Exception ex)
		{
			status = false;
			statusMessage = "List of Domains can't be fetched..";

			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}


		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}



	// Save Domain details
	@RequestMapping(value = "/api/domain/save_domain", method = RequestMethod.POST)
	public ResponseEntity<?> saveDomain(@RequestBody Domain domain) 
	{
		Map<String, Object> data = new HashMap<String,Object>();

		//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		String domain_tags = domain.getTags();
		String domain_name = domain.getName();
		//System.out.println("Domain Name & tags"+domain_name+domain_tags);

		//Getting already stored domain lists
		List<Domain> listOfDomains = domainService.listAll();

		//Checking Duplicate Domian Name entry
		boolean isDuplicateDomainExists = checkDuplicateDomainEntry(listOfDomains,domain_name);
		if(isDuplicateDomainExists)
		{
			//log.info("This domain is already exists ..... Please enter any other domain");
			status = false;
			statusMessage = "This domain is already exists ..... Please enter any other domain";

			//			mav.addObject("domain", domain);
			//			mav.addObject("status",status);
			//			mav.addObject("statusMessage", statusMessage);

			data.put("domain", domain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);


		}

		else
		{


			//Setting Domain Status
			domain.setStatus("ACTIVE");

			//System.out.println("New Domain"+domain);
			status = domainService.save(domain);


			if(status)
			{
				status = true;
				statusMessage = "Domain is saved successfully!!!";

				//				mav.addObject("domain", domain);
				//				mav.addObject("status",status);
				//				mav.addObject("statusMessage", statusMessage);

				data.put("domain", domain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}

			else
			{
				status = false;
				statusMessage = "Something went wrong.... Please try again later!!!";

				//				mav.addObject("domain", domain);
				//				mav.addObject("status",status);
				//				mav.addObject("statusMessage", statusMessage);

				data.put("domain", domain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}


		}

		//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

	}
	
	
	
//	// Update Domain origin
	
	
//		@RequestMapping(value = "api/domain/update_domain",method = RequestMethod.PATCH)
//		public ResponseEntity<?> updateDomain(@RequestBody Domain domain)
//		{
//			//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
//			Map<String, Object> data = new HashMap<String,Object>();
//
//			System.out.println("Domain details"+domain);
//			String domain_name = domain.getName();
//
//
//			System.out.println("Domain Name type"+domain_name);
//			List<Domain> listOfDomains = domainService.listAll();
//			boolean isDuplicateDomainExists = checkDuplicateDomainEntry(listOfDomains,domain_name);
//			if(isDuplicateDomainExists)
//			{
//				System.out.println("This domain is already exists for other id ..... Please enter any other domain");
//				status = false;
//				statusMessage = "This domain is already exists for other id..... Please enter any other domain";
//
//				//			mav.addObject("domain", domain);
//				//			mav.addObject("status",status);
//				//			mav.addObject("statusMessage", statusMessage);
//
//				data.put("domain", domain);
//				data.put("status", status);
//				data.put("statusMessage", statusMessage);
//
//
//			}
//
//			else
//			{
//
//				//	boolean status = false;
//
//				System.out.println("New Domain name:"+domain.getName());
//				Domain domainobj = domainService.get(domain.getId());
//				domain.setTags(domainobj.getTags());
//				domain.setStatus(domainobj.getStatus());
//				status = domainService.save(domain);
//
//				if(status)
//				{
//					status = true;
//					statusMessage = "Domain is updated successfully!!!";
//
//					//				mav.addObject("domain", domain);
//					//				mav.addObject("status",status);
//					//				mav.addObject("statusMessage", statusMessage);
//
//					data.put("domain", domain);
//					data.put("status", status);
//					data.put("statusMessage", statusMessage);
//				}
//
//				else
//				{
//					status = false;
//					statusMessage = "Something went wrong.... Please try again later!!!";
//
//					//				mav.addObject("domain", domain);
//					//				mav.addObject("status",status);
//					//				mav.addObject("statusMessage", statusMessage);
//
//					data.put("domain", domain);
//					data.put("status", status);
//					data.put("statusMessage", statusMessage);
//				}
//
//
//			}
//			//		return mav;
//			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
//		}

	// Update Domain change by Ashish
	@RequestMapping(value = "api/domain/update_domain",method = RequestMethod.PATCH)
	public ResponseEntity<?> updateDomain(@RequestBody Domain domain)
	{
		//System.out.println("---- I am here---");
		//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();

		//System.out.println("Domain details"+domain);
		String domain_name = domain.getName();
		//log.debug("------- domain isexists: "+domainService.isExistsDomain(domain.getName()));
		//log.info("-------- Tage isExists: "+domainService.isExistsTag(domain.getTags()));
		//		System.out.println("Domain Name type"+domain_name);
		//		List<Domain> listOfDomains = domainService.listAll();
		//		boolean isDuplicateDomainExists = checkDuplicateDomainEntry(listOfDomains,domain_name);
		//		if(isDuplicateDomainExists)
		//		{
		//			System.out.println("------------>>>This domain is already exists for other id ..... Please enter any other domain");
		//			status = false;
		//			statusMessage = "This domain is already exists for other id..... Please enter any other domain";
		//
		//			//			mav.addObject("domain", domain);
		//			//			mav.addObject("status",status);
		//			//			mav.addObject("statusMessage", statusMessage);
		//
		//			data.put("domain", domain);
		//			data.put("status", status);
		//			data.put("statusMessage", statusMessage);
		//
		//
		//		}
		//	
		//		else

		if(domainService.isExistsDomain(domain.getName().trim())&& (!domainService.isExistsTag(domain.getTags()))){

			Domain domainobj = domainService.get(domain.getId());
			domainobj.setTags(domain.getTags());
			//domainobj.setStatus("ACTIVE");
			domain.setStatus(domainobj.getStatus());
//			status = domainService.save(domain);
			domainService.save(domain);
			data.put("statusMessage", statusMessage);
			statusMessage="Tag is updated successfully";
			data.put("domain", domain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

		}


		else if (!domainService.isExistsDomain(domain.getName().trim()))
		{	
			//System.out.println("------>  ------> New Domain name:"+domain.getName());
			Domain domainobj = domainService.get(domain.getId());
			domainobj.setName(domain.getName().trim());
//			domainobj.setTags(domain.getTags());
//			domainobj.setStatus("ACTIVE");
			domain.setStatus(domainobj.getStatus());
			status= domainService.save(domain);
			
			statusMessage="Domain is updated successfully";
			data.put("domain", domain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
		}

		else  {
			//System.out.println("------------------------- I am here----");
			Domain domainobj = domainService.get(domain.getId());
			if(domainService.isExistsDomain(domain.getName().trim())) {
				domainobj.setStatus("ACTIVE");
				status=true;
				statusMessage="Domain is duplicate";
				data.put("domain", domain);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				
			}
			if(domainobj.getTags().equals(domain.getTags())) {
				//System.out.println("-------i AM HERE-----");
				status = false;
				statusMessage = "This domain is already exists ...... Please enter any other domain";

				data.put("domain", domain);
				domain.setStatus("ACTIVE");
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				//statusMessage="Both domain and Tag is already present in database";
			}

			else if(!(domainobj.getTags().equals(domain.getTags()))) {
				domainobj.setTags(domain.getTags());
				status=domainService.save(domain);
				statusMessage="kindly Update the tag first and refresh the page";
			}
			status=false;
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

		}
		//		else {
		//			statusMessage="Something went wrong";
		//			status=false;
		//			data.put("status", status);
		//			data.put("statusMessage", statusMessage);
		//			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
		//		}

		//				//boolean status = true;
		//
		//				System.out.println("------>  ------> New Domain name:"+domain.getName());
		//				Domain domainobj = domainService.get(domain.getId());
		//				domainobj.setTags(domain.getTags());
		//				domainobj.setStatus(domain.getStatus());
		//				domainobj.setName(domain.getName());
		//				status = domainService.save(domainobj);
		//				data.put("statusMessage", statusMessage);
		//
		//
		//				if(status) {
		//					status= true;
		//					statusMessage= "Tag is Updated Successfully";
		//					data.put("domain", domain);
		//					data.put("status", status);
		//					data.put("statusMessage", statusMessage);
		//
		//				}
		//
		//				if(status)
		//				{
		//					status = true;
		//					statusMessage = "Domain is updated successfully!!!";
		//
		//					//				mav.addObject("domain", domain);
		//					//				mav.addObject("status",status);
		//	0				//				mav.addObject("statusMessage", statusMessage);
		//
		//					data.put("domain", domain);
		//					data.put("status", status);
		//					data.put("statusMessage", statusMessage);
		//				}
		//
		//				else
		//				{
		//					status = false;
		//					statusMessage = "Something went wrong.... Please try again later!!!";
		//
		//					//				mav.addObject("domain", domain);
		//					//				mav.addObject("status",status);
		//					//				mav.addObject("statusMessage", statusMessage);
		//
		//					data.put("domain", domain);
		//					data.put("status", status);
		//					data.put("statusMessage", statusMessage);
		//				}
		//
		//			}
		//		}
		//
		//		//		return mav;
		//		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}



	// Check Domain Dupliacte Entry
	public boolean checkDuplicateDomainEntry(List<Domain> listOfDomains, String domain_name)
	{
		boolean isDuplicate=false;
		for(int j=0;j<listOfDomains.size();j++)
		{

			Domain domainobj = (Domain) listOfDomains.get(j);
			if(domain_name.compareToIgnoreCase(domainobj.getName())==0)
			{
				isDuplicate = true;
			}


		}

		return isDuplicate;

	}




	//Edit Domain Details
	@RequestMapping("/api/domain/getByDomainId/{id}")
	public ResponseEntity<?> showEditDomainPage(@PathVariable(name = "id") int id) 
	{

		//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();

		try
		{
			Domain domain = domainService.get(id);
			status = true;
			statusMessage = "Domain has been fetched successfully!!!";

			//			mav.addObject("domain", domain);
			//			mav.addObject("status", status);
			//			mav.addObject("statusMessage", statusMessage);

			data.put("domain", domain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}


		catch(Exception ex)
		{
			Domain domain = new Domain();
			status = false;
			statusMessage = "Something went wrong...Domain is not found!!!!!!";

			//			mav.addObject("domain", domain);
			//			mav.addObject("status", status);
			//			mav.addObject("statusMessage", statusMessage);

			data.put("domain", domain);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}

		//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}


	//Delete Domain details
	@RequestMapping(value = "/api/domain/delete_domain/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDomain(@PathVariable(name = "id") int id) 
	{
		//		ModelAndView mav =  new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();


		boolean isHavingSubdomain = false;
		isHavingSubdomain = subdomainService.getSubdomainsByDomainId(id);
		if(!isHavingSubdomain) 
		{  
			try
			{
				int flag = 0;
				List<Userlogin> usersList = userloginService.listAll();
				for(int i=0;i<usersList.size();i++)
				{

					Userlogin userobj = usersList.get(i);
					System.out.println("Users list by Domain id"+userobj);

					int role_id = userobj.getFk_role_id();

					if(role_id==2)
					{
						String domain_id_db = userobj.getFk_domain_id();
						int domain_id = Integer.parseInt(domain_id_db);
						if(domain_id==id)
						{
							flag=2;
						}

					}

					else if(role_id==3)
					{
						String domain_ids = userobj.getFk_domain_id();
						String[] domain_id_array = domain_ids.split(",");
						for(int j=0;j<domain_id_array.length;j++)
						{
							int domain_id_array_ele = Integer.parseInt(domain_id_array[j]);
							if(domain_id_array_ele==id)
							{
								flag = 3;

							}
						}
					}


				}

				if(flag==2)
				{
					status = false;
					statusMessage = "Ooops This domain is assigned to a Content Creator...Please delete content creator first to delete this domain!!!";

					//					mav.addObject("status", status);
					//					mav.addObject("statusMessage", statusMessage);

					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}

				else if(flag==3)
				{

					status = false;
					statusMessage = "Ooops This domain is assigned to a learner..Please delete the learner first to delete this domain !!!";

					//					mav.addObject("status", status);
					//					mav.addObject("statusMessage", statusMessage);

					data.put("status", status);
					data.put("statusMessage", statusMessage);

				}

				else
				{

					status = domainService.delete(id);
					if(status)
					{
						status = true;
						statusMessage = "Domain is deleted successfully!!!";

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}

					else
					{
						status = false;
						statusMessage = "Domain not deleted successfully!!!";

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}


				}


			}

			catch(Exception ex)
			{
				System.out.println("Exception is:"+ex.getMessage());

				status = false;
				statusMessage = "Something went wrong...Please try again later!!!";

				//				mav.addObject("status", status);
				//				mav.addObject("statusMessage", statusMessage);

				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}

		else
		{
			status = false;
			statusMessage = "This domain is having subdomain...Please delete subdomain first to delete this domain!!!";

			//			mav.addObject("status", status);
			//			mav.addObject("statusMessage", statusMessage);

			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}


		//		return mav;  
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}


}

