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

import net.codejava.entities.Content;
import net.codejava.entities.Domain;
import net.codejava.entities.Userlogin;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.UserloginService;


@RestController
@CrossOrigin("*")
public class ContentController {

	boolean status = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(ContentController.class);

	@Autowired
	private DomainService domainService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private UserloginService userloginService;

//	@ModelAttribute
//	public void setVaryResponseHeader(HttpServletResponse response) {
//		//response.setHeader("Access-Control-Allow-Origin", "false");
//		//response.setHeader("Access-Control-Allow-Origin","http://192.168.0.173:8080/new_domain_page");
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, PATCH, OPTIONS");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//		response.setHeader("Access-Control-Allow-Credentials","true");
//	} 



	//Content handler methods starts.....//

	// Testing ...... file uploading
	@RequestMapping(value = "/api/content/upload_file", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String domain_name,@RequestParam String subdomain_name) throws IOException
	{

		String content_path = "DomainApp/content/"+domain_name+"/"+subdomain_name;
		Path path = Paths.get(content_path);
		System.out.println("New path for filing:"+path);

		if(!Files.exists(path))
		{
			Files.createDirectories(path);


		}

		String fileOriginalName = file.getOriginalFilename();

		if(fileOriginalName.endsWith(" "))
		{
			fileOriginalName = fileOriginalName.substring(0, fileOriginalName.length()-1);
		}

		File convertedFile = new File(path+"\\"+fileOriginalName);

		//	String content_actual_path = path+"/"+file.getOriginalFilename();
		String content_actual_path="";

		if(convertedFile.createNewFile())
		{
			try(FileOutputStream fileOut = new FileOutputStream(convertedFile))
			{

				fileOut.write(file.getBytes());
				status = true;
				statusMessage = "File uploaded successfully!!!";

				content_actual_path= convertedFile.getAbsolutePath();
				System.out.println("Testing path from file uploading:"+content_actual_path);

			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				status = false;
				statusMessage = "File is very large...can't be uploaded!!!";

				content_actual_path= convertedFile.getAbsolutePath();
				System.out.println("Testing path from file uploading:"+content_actual_path);
			}
		}
		else
		{
			status = false;
			statusMessage = "File in this particular directory already exists...content not saved!!!";
			content_actual_path = "No path available";
		}




//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
//		mav.addObject("status", status);
//		mav.addObject("statusMessage",statusMessage);
//		mav.addObject("content_actual_path", content_actual_path);
		
		data.put("content_actual_path", content_actual_path);
		data.put("status", status);
		data.put("statusMessage", statusMessage);
		
//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}



	// Testing ..... file uploading ends




	// Save User along with content uploading  By User himself/herself details

	@RequestMapping(value = "/api/content/save_content", method = RequestMethod.POST)
	public ResponseEntity<?> uploadContent(@RequestBody Content content)
	{

		// Date Generating using Simple Date Format & setting to entity class
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		Date date = new Date();
		String uploading_date_time = sdf.format(date);
		content.setUploading_date_time(uploading_date_time);

		System.out.println("Content from Front end:"+content);


		if(content.getContent_type().equals("FILE"))
		{
			content.setContent_URL("NA");
			statusMessage = contentService.save(content);
			if(statusMessage.equals(null)||statusMessage.equals(""))
			{
				status = true;
				statusMessage = "File uploaded & Content Details Saved successfully!!!";
			}
			else
			{
				status = false;
				statusMessage = "File Uploaded but Content not saved....Something went wrong!!! ";
			}

		}

		else if(content.getContent_type().equals("LINK"))
		{
			content.setContent_size(" ");
			content.setContent_extension("NA");
			content.setContent_actual_path("NA");
			statusMessage = contentService.save(content);
			if(statusMessage.equals(null)||statusMessage.equals(""))
			{
				status = true;
				statusMessage = "File uploaded & Content Details Saved successfully!!!";
			}
			else
			{
				status = false;
				statusMessage = "File Uploaded but Content not saved....Something went wrong!!! ";
			}

		}

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		
//		mav.addObject("content", content);
//		mav.addObject("status", status);
//		mav.addObject("statusMessage", statusMessage);
		
		
		data.put("content", content);
		data.put("status", status);
		data.put("statusMessage", statusMessage);
		
//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

	}



	// Get Content Details using user id(Mainly content creator ID) from Content Table in DB // Domain & subdomain Name required to join
	@RequestMapping("/api/content/getContentDetailsByUserID/{fk_user_id}")
	public ResponseEntity<?> getContentDetailsUsingUserID(@PathVariable(name = "fk_user_id") int fk_user_id)
	{
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			List<ContentUserloginSubdomainDomainPayload> listOfContentsByUserId = contentService.findContentByUserId(fk_user_id);
			System.out.println("-------------- size is "+listOfContentsByUserId.size());
			if(!listOfContentsByUserId.isEmpty())
			{
				status = true;
				statusMessage = "Retrieving contents successfully!!!";

//				mav.addObject("listOfContentsByUserId", listOfContentsByUserId);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("listOfContentsByUserId", listOfContentsByUserId);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
			else
			{
				status = false;
				statusMessage = "No Content uploaded by this particular user!!!";

//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

		}
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something went wrong, please try again later!!!";

//			mav.addObject("status", status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			
			
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

	}

	// Get Content Details using content id from Content Table in DB // Domain Name & Subdomain name
	@RequestMapping("/api/content/getContentDetailsByContentID/{content_id}")
	public ResponseEntity<?> getContentDetailsUsingContentID(@PathVariable(name = "content_id") int content_id)
	{
		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			ContentUserloginSubdomainDomainPayload contentsObjByContentId = contentService.findContentByContentid(content_id);
			if(contentsObjByContentId!=null)
			{
				status = true;
				statusMessage = "Retrieving contents successfully!!!";

//				mav.addObject("contentsObjByContentId", contentsObjByContentId);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("contentsObjByContentId", contentsObjByContentId);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
			else
			{
				status = false;
				statusMessage = "No Content uploaded against this particular content id!!!";

//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

		}
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something went wrong, please try again later!!!";

//			mav.addObject("status", status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
	}





	//Download Content
	@RequestMapping("/api/content/download_content/{content_id}")
	public ResponseEntity<?> downloadContent( HttpServletRequest request,HttpServletResponse response, @PathVariable(name = "content_id") int content_id) throws IOException
	{
		//System.out.println("check cc_id before download:"+content.getContent_actual_path());
		System.out.println("Content id against a particular content "+ content_id);
		String content_actual_path = "";
//		ModelAndView mav =  new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			Content contentobj = contentService.get(content_id);
			System.out.println("check content_actual_path before download:"+contentobj.getContent_actual_path());

			if(contentobj!=null && contentobj.getContent_id()>0)
			{
				content_actual_path = contentobj.getContent_actual_path();
				String filename_with_directory = content_actual_path;
				File file_name = new File(content_actual_path);
				System.out.println("File name with extension:"+file_name.getName());
				Path file = Paths.get(filename_with_directory);
				if (Files.exists(file)) 
				{

					String mimeType = Files.probeContentType(file);
					System.out.println("file mimetype:"+mimeType);
					if (mimeType == null) {
						//unknown mimetype so set the mimetype to application/octet-stream
						mimeType = "application/octet-stream";
					}

					response.setContentType(mimeType);
					//					response.addHeader("Content-Disposition", "attachment; filename="+file);
					response.addHeader("Content-Disposition", "attachment; filename="+file_name.getName());
					try
					{
						Files.copy(file, response.getOutputStream());
						response.getOutputStream().flush();
					} 
					catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			}

			else
			{
				status = false;
				statusMessage = "No content is there against your content id!!! ";
				
//				mav.addObject("content_actual_path", content_actual_path);
//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("content_actual_path", content_actual_path);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}


		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something went wrong!!! ";
			
//			mav.addObject("content_actual_path", content_actual_path);
//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("content_actual_path", content_actual_path);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);


	}


	// Username rquired
	@RequestMapping("/api/content/getContentsByDomainId/{fk_domain_id}")
	public ResponseEntity<?> viewContents(@ModelAttribute("fk_domain_id")int fk_domain_id)
	{

//		ModelAndView mav= new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			List<ContentUserloginSubdomainDomainPayload> listOfContentsByDomainId = contentService.findContentByDomainId(fk_domain_id);
			if(!listOfContentsByDomainId.isEmpty())
			{
				status = true;
				statusMessage = "Retrieving contents successfully!!!";

//				mav.addObject("listOfContentsByDomainId", listOfContentsByDomainId);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("listOfContentsByDomainId", listOfContentsByDomainId);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
			else
			{
				status = false;
				statusMessage = "No Content uploaded against this particular Domain!!!";

//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}

		}
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Something went wrong, Please try again later!!!";

//			mav.addObject("status", status);
//			mav.addObject("statusMessage", statusMessage);
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

//		return mav;	
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);


	}



	
	// Delete Content
	@RequestMapping(value = "/api/content/delete_content/{content_id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContent(@PathVariable(name = "content_id") int content_id) 
	{
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			//Delete Content from file storage
			Content contentobj = contentService.get(content_id);
			String content_actual_path = contentobj.getContent_actual_path();
			if(content_actual_path.equalsIgnoreCase("NA"))
			{
				//Delete Content details from db
				contentService.delete(content_id);
				
				
				status = true;
				statusMessage = "Link deleted successfully!!!";
				
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			
			else
			{
				//Delete File from file storage
				File file = new File(content_actual_path);
				file.delete();
				
				//Delete Content details from db
				contentService.delete(content_id);
				
				
				status = true;
				statusMessage = "File deleted successfully!!!";
				
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				
			}
			
			
			
			
		}
		
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Content is missing ...something went wrong!!!";
			
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			
		}
		
		
		
//		return mav;
		return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);

	}
	
	
	
	//Listen Content
		@RequestMapping("/api/content/content_text/{content_id}")
		public ResponseEntity<?> listenContent(@PathVariable(name = "content_id") int content_id) throws IOException
		{
			
//			ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
			Map<String, Object> data = new HashMap<String,Object>();
			String content = "";
			try
			{
				Content contentobj = contentService.get(content_id);
				String content_path = contentobj.getContent_actual_path();
				String content_ext = contentobj.getContent_extension();
				content = contentService.listenToContent(content_path, content_ext);
				if(content!=null)
				{
					status = true;
					statusMessage = "Text fetched successfully!!!";
					
//					mav.addObject("content", content);
//					mav.addObject("status", status);
//					mav.addObject("statusMessage", statusMessage);
					
					data.put("content", content);
					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
				else
				{
					status = false;
					statusMessage = "no text is there!!!";
					
//					mav.addObject("content", content);
//					mav.addObject("status", status);
//					mav.addObject("statusMessage", statusMessage);
					
					data.put("content", content);
					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
			}
			
			catch(Exception ex)
			{	
				status = false;
				statusMessage = "Text not fetched..something went wrong";
				
//				mav.addObject("content", content);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("content", content);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
			
//			return mav;
			return new ResponseEntity<Map<String, Object>>(data,HttpStatus.OK);
		}
		
		
	

	 
}
