package net.codejava.controllers;

import java.io.File;
import java.io.FileInputStream;
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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
import net.codejava.entities.DomainFilePath;
import net.codejava.entities.Userlogin;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;
import net.codejava.services.ChartService;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.TagService;
import net.codejava.services.UserloginService;


@RestController
@CrossOrigin("*")
public class TagController {

	boolean status = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(TagController.class);

		
	@Autowired
	private TagService tagService;
	
	@Autowired
	private DomainService domainService;
	
	// Create Tags
		@RequestMapping(value = "/api/tags/create_tag",method = RequestMethod.POST)
		public ResponseEntity<?> createTags(@RequestBody DomainFilePath domainFilePath)
		{
			Map<String, Object> data = new HashMap<String,Object>();
			try 
	        {
				List<String> listOfSelectedTags = new ArrayList<String>();
				String allTags = domainService.getTagsByDomainId(domainFilePath.getDomainId());
				String filePath = domainFilePath.getFilePath();
				
				String extension = "";
				if(filePath.contains(".")) 
				{
				    extension = filePath.substring(filePath.lastIndexOf(".")+1);
				}
				
				else
				{
					extension = "LINK";
				}
				
				
					if(extension.equals("pdf"))
					{
						File file = new File(filePath);
						PDDocument doc = PDDocument.load(file);  
					      
				        //Instantiate PDFTextStripper class  
				        PDFTextStripper pdfStripper = new PDFTextStripper();  
	
				        //Retrieving text from PDF document  
				        String text = pdfStripper.getText(doc);
				        System.out.println("text is "+text);
				        String[] textAll = text.split(" ");
			            String[] inputs = allTags.split(",");
			            listOfSelectedTags = tagService.tagsHashMapCreation(inputs,textAll);
					}
					
					else if (extension.equals("doc")||extension.equals("docx"))
					{
						FileInputStream fis = new FileInputStream(filePath);
			            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
			            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
	
			            //Printing the text
			            String text = extractor.getText();
					}
					
					else if(extension.equals("txt"))
					{
						Path path = Paths.get(filePath);
			            String text = new String(Files.readAllBytes(path));
			            String[] textAll = text.split(" ");
			            String[] inputs = allTags.split(",");
			            listOfSelectedTags = tagService.tagsHashMapCreation(inputs,textAll);
	
					}
					
				
	            
	            
	            if(!listOfSelectedTags.isEmpty())
	            {
	            	status = true;
		            statusMessage = "List of tags created successfully";
		            
		            data.put("listOfSelectedTags", listOfSelectedTags);
		            data.put("status", status);
					data.put("statusMessage", statusMessage);
	            }
	            
	            else
	            {
	            	status = false;
		            statusMessage = "No tag created....";
		            
		            data.put("status", status);
					data.put("statusMessage", statusMessage);
	            }
				
	        } 
	  
	        catch (Exception e) { 
//	            e.printStackTrace();
	        	
	        	status = false;
	            statusMessage = "No tag created....Something went wrong!!!";
	            
	        	data.put("status", status);
				data.put("statusMessage", statusMessage);
	        }
						
			return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
		}
		
		
		
	 
}
