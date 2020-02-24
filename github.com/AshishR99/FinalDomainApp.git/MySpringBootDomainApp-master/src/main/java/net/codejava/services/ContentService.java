package net.codejava.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.transaction.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.codejava.entities.Content;
import net.codejava.entities.Userlogin;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;
import net.codejava.repositories.ChartRepository;
import net.codejava.repositories.ContentRepository;
import net.codejava.repositories.ContentUserloginSubdomainDomainRepository;


@Service
@Transactional
public class ContentService {

	String errorMessage = "";
	boolean status = false;

	@Autowired
	private ContentRepository contentRepo;
	
	@Autowired
	private ContentUserloginSubdomainDomainRepository contentUserloginSubdomainDomainRepo;
	
	@Autowired
	private ChartRepository chartRepo;

	public List<Content>listAll() {
		return contentRepo.findAll();
	}

	public String  save(Content content) {

		try
		{
			contentRepo.save(content);
			errorMessage = "";
		}

		catch(Exception ex)
		{
			errorMessage = "Content details not uploaded successfully!!!";
		}

		return errorMessage;
	}

	public Content get(int id) {
		return contentRepo.findById(id).get();
	}

	public void delete(int id) {
		
		contentRepo.deleteById(id);
		
	}
	
	public boolean findContentsUsingUserId(int fk_user_id)
	{
		List<Content> listContentsUsingUserId = contentRepo.findContentsByUserId(fk_user_id);
		if(!listContentsUsingUserId.isEmpty())
		{
			status = true;
		}
		else
		{
			status = false;
		}
		
		return status;
	}


	public List<ContentUserloginSubdomainDomainPayload> findContentByUserId(int fk_user_id)
	{
		List<ContentUserloginSubdomainDomainPayload> listContentsUsingUserId = contentUserloginSubdomainDomainRepo.getContentsByUserId(fk_user_id);
		return listContentsUsingUserId;
	}
	
	public List<ContentUserloginSubdomainDomainPayload> findContentByDomainId(int fk_domain_id)
	{
		List<ContentUserloginSubdomainDomainPayload> listContentsUsingDomainId = contentUserloginSubdomainDomainRepo.getContentsByDomainId(fk_domain_id);
		return listContentsUsingDomainId;
	}
	
	public ContentUserloginSubdomainDomainPayload findContentByContentid(int content_id)
	{
		ContentUserloginSubdomainDomainPayload contentObjsUsingContentId = contentUserloginSubdomainDomainRepo.getContentsByContentId(content_id);
		return contentObjsUsingContentId;
	}

	

	public List<Content> findContentBySubdomainId(int fk_subdomain_id)
	{

		return contentRepo.findContentDetailsBySubdomainId(fk_subdomain_id);
		
	}
	
	
	//Convert file into String as text
	public String listenToContent(String content_path, String content_extension)
	{
		String content = "";
		
		if(content_extension.equalsIgnoreCase("txt"))
		{
			try
			{
				String filepath = content_path;
	            Path path = Paths.get(filepath);
	            String text = new String(Files.readAllBytes(path));
	            System.out.println(text);
	            
	            content = text;
			}
			catch(Exception ex)
			{
				content = "";
			}
		}
		
		else if(content_extension.equalsIgnoreCase("pdf")) 
		{
			try
			{
				File file = new File(content_path);
				PDDocument doc = PDDocument.load(file);  
			      
		        //Instantiate PDFTextStripper class  
		        PDFTextStripper pdfStripper = new PDFTextStripper();  

		        //Retrieving text from PDF document  
		        String text = pdfStripper.getText(doc); 
		        System.out.println(text);
		        
//		     // Set property as Kevin Dictionary 
//                System.setProperty("freetts.voices","com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory"); 
//
//                // Register Engine 
//                Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral"); 
//
//                // Create a synthesizer for English
//                Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.ENGLISH));
//                
//                // Allocate synthesizer 
//                synthesizer.allocate(); 
//
//                // Resume Synthesizer 
//                synthesizer.resume(); 
//
//                // Speaks the given text 
//                // until the queue is empty. 
//                synthesizer.speakPlainText(text, null); 
//                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
		        
                content = text;
			}
			catch(Exception ex)
			{
				content = "";
			}
			
		}
		
		else if(content_extension.equalsIgnoreCase("doc") || content_extension.equalsIgnoreCase("docx"))
		{
			try
			{
				FileInputStream fis = new FileInputStream(content_path);
	            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
	            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);

	            //Printing the text
	            String text = extractor.getText();
	            System.out.println(text);
	            
	            content = text;
	            
			}
			
			catch(Exception ex)
			{
				content = "";
			}
		}
		
		return content;
	}
	


	

}
