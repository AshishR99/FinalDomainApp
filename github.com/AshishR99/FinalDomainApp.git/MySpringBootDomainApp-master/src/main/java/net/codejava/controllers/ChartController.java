package net.codejava.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.core.sym.Name;

import net.codejava.entities.ChartDataBasedOnLevel;
import net.codejava.entities.Content;
import net.codejava.entities.Domain;
import net.codejava.entities.Userlogin;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;
import net.codejava.services.ChartService;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.UserloginService;


@RestController
@CrossOrigin("*")
public class ChartController {

	boolean status = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(ChartController.class);

	@Autowired
	private DomainService domainService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private UserloginService userloginService;
	
	@Autowired
	private ChartService chartService;

//	@ModelAttribute
//	public void setVaryResponseHeader(HttpServletResponse response) {
//		//response.setHeader("Access-Control-Allow-Origin", "false");
//		//response.setHeader("Access-Control-Allow-Origin","http://192.168.0.173:8080/new_domain_page");
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, PATCH, OPTIONS");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//		response.setHeader("Access-Control-Allow-Credentials","true");
//	} 



	
		
	// Chart Domain vs No of contents
		@RequestMapping("/api/chart/domain_content")
		public ResponseEntity<?> makeChartDomainvsNoOfContents()
		{
//			ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
			Map<String, Object> data = new HashMap<String,Object>();
			
			List<Integer> listOfDomainIds = new ArrayList<Integer>(); 
			HashMap<String, Integer> contentDomainHashMap = new HashMap<String, Integer>();
			int noOfContentsByDomain = 0;
			try
			{
				List<Domain> listOfDomains = domainService.listAll();
				for(Domain domainObj:listOfDomains)
				{
					System.out.println("List of Domain Ids"+domainObj);
					listOfDomainIds.add(domainObj.getId());
					
				}
				
				System.out.println("List of Domain Ids"+listOfDomainIds);
				for(Integer domain_id:listOfDomainIds)
				{
					noOfContentsByDomain = chartService.countContentByDomain(domain_id);
					String domain_name = domainService.getDomainNameByDomainId(domain_id);
					contentDomainHashMap.put(domain_name, noOfContentsByDomain);
				}
				
				System.out.println("Hash map "+contentDomainHashMap);
				status = true;
				statusMessage = "No of contents against";
//				

//				mav.addObject("contentDomainHashMap", contentDomainHashMap);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("contentDomainHashMap", contentDomainHashMap);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			
				
			}
			catch(Exception ex)
			{
				status = false;
				statusMessage = "content not fetched..something went wrong";
				
//				
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("contentDomainHashMap", contentDomainHashMap);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			
			
			
			
			return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
		}
		
		
		
		// Chart Domain -- Level vs No of contents
		//Under testing
		@RequestMapping("/api/chart/domain_level_content")
		public ResponseEntity<?> makeChartDomainLevelvsNoOfContents()
		{

			Map<String, Object> data = new HashMap<String,Object>();
			
			String[] contentLevels = {"BASIC","INTERMEDIATE","ADVANCE"};
			List<Integer> listOfDomainIds = new ArrayList<Integer>();
			ChartDataBasedOnLevel chartDataClass= new ChartDataBasedOnLevel();
			List<ChartDataBasedOnLevel> chartDataListBasedOnLevel = new ArrayList<ChartDataBasedOnLevel>();
			HashMap<String, Integer> hm = new HashMap<String, Integer>();

			
			try
			{
				List<Domain> listOfDomains = domainService.listAll();
				for(Domain domainObj:listOfDomains)
				{
					System.out.println("List of Domain Ids"+domainObj);
					listOfDomainIds.add(domainObj.getId());
					
				}
				
				System.out.println("List of Domain Ids"+listOfDomainIds);
				for(Integer domain_id:listOfDomainIds)
				{
				
					chartDataClass = chartService.countContentByDomainLevel(domain_id);
					System.out.println("Chart Data"+chartDataClass);
					chartDataListBasedOnLevel.add(chartDataClass);
					
				}
				
				
				
				status = true;
				statusMessage = "Chart Data fetched successfully!!!";
				
				
//				mav.addObject("chartDataHashMap",chartDataHashMap);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("chartDataListBasedOnLevel",chartDataListBasedOnLevel);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				
			}
			
			
			catch(Exception ex)
			{
				status = false;
				statusMessage = "Chart data not fetched..something went wrong";
				
				
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("chartDataListBasedOnLevel",chartDataListBasedOnLevel);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		
			
//			return mav;
			return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
		}

		
		// Extension based chart data
		@RequestMapping("/api/chart/domain_extension_content/{user_id}")
		public ResponseEntity<?> makeChartContentExtensionvsNoOfContents(@PathVariable(name = "user_id") int user_id)
		{
			List<Integer> listOfDomainIds = new ArrayList<Integer>();
			Map<String, Object> data = new HashMap<String,Object>();
			HashMap<String,Integer> chartDataMapBasedOnExtension = new HashMap<String, Integer>();
			List<HashMap<String, Integer>> chartDataListBasedOnExtension = new ArrayList<HashMap<String,Integer>>();
			try
			{
				String domain_id_in_string = userloginService.findDomainByUserId(user_id);
				String[] domain_ids = domain_id_in_string.split(",");
				
				
				List<Domain> listOfDomains = domainService.listAll();
				for(Domain domainObj:listOfDomains)
				{
					System.out.println("List of Domain Ids"+domainObj);
					listOfDomainIds.add(domainObj.getId());
					
				}
				
				
				for(String domainId:domain_ids)
				{
					System.out.println(domainId);
					if(domainId.equalsIgnoreCase("NA"))
					{
						for(Integer domain_id:listOfDomainIds)
						{
							chartDataMapBasedOnExtension = chartService.countContentByExtension(domain_id);
							if(!chartDataMapBasedOnExtension.isEmpty()) 
							{
								chartDataListBasedOnExtension.add(chartDataMapBasedOnExtension);
							}
						}
					}
					
					else
					{
						int domain_id = Integer.parseInt(domainId);
						chartDataMapBasedOnExtension = chartService.countContentByExtension(domain_id);
						if(!chartDataMapBasedOnExtension.isEmpty()) 
						{
							chartDataListBasedOnExtension.add(chartDataMapBasedOnExtension);
						}
					}
					
					
				}
				
				status = true;
				statusMessage = "Chart data based on Content extension fetched successfully!!!";
				
				data.put("chartDataListBasedOnExtension", chartDataListBasedOnExtension);
				data.put("status",status);
				data.put("statusMessage", statusMessage);
				
			}
			
			catch(Exception ex)
			{
				status = false;
				statusMessage = "Chart data cant be fetched successfully!!!";
				
				data.put("chartDataListBasedOnExtension", chartDataListBasedOnExtension);
				data.put("status",status);
				data.put("statusMessage", statusMessage);
			}
			
//			chartService.countContentByExtension(1);
			
			return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
		}
	 
}
