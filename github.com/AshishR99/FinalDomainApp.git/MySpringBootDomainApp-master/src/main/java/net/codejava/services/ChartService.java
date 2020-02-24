package net.codejava.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.ChartDataBasedOnLevel;
import net.codejava.entities.Content;
import net.codejava.repositories.ChartRepository;
import net.codejava.repositories.ContentRepository;

@Service
public class ChartService {

	@Autowired
	private ChartRepository chartRepo;
	
	@Autowired
	private DomainService domainService;
	
	@Autowired
	private ContentRepository contentRepo;
	
	
	//Create Chart Content against a particular domain
	public int countContentByDomain(int domain_id)
	{
		int count = 0;
		List<Content> listOfContents = chartRepo.findByDomainId(domain_id);
		count = listOfContents.size();
		return count;
	}
		
	
	//Create Chart Content and level against a particular domain
	public ChartDataBasedOnLevel countContentByDomainLevel(int domain_id)
	{
		HashMap<String, Integer> domainLevelBasedContentsHmap = new HashMap<String, Integer>();
		ChartDataBasedOnLevel chartDataClass= new ChartDataBasedOnLevel();
		
		String[] contentLevels = {"BASIC","INTERMEDIATE","ADVANCE"};
		
		List<Content> listOfBasicContents = chartRepo.findByDomainLevel(domain_id,contentLevels[0]);
		domainLevelBasedContentsHmap.put(contentLevels[0], listOfBasicContents.size());
		
		List<Content> listOfIntermediateContents = chartRepo.findByDomainLevel(domain_id,contentLevels[1]);
		domainLevelBasedContentsHmap.put(contentLevels[1], listOfIntermediateContents.size());
		
		List<Content> listOfAdvanceContents = chartRepo.findByDomainLevel(domain_id,contentLevels[2]);
		domainLevelBasedContentsHmap.put(contentLevels[2], listOfAdvanceContents.size());
		
		
		System.out.println(domainLevelBasedContentsHmap);
		
		String domain_name = domainService.getDomainNameByDomainId(domain_id);
		chartDataClass.setDomain_name(domain_name);
		chartDataClass.setDomainContentsHashmap(domainLevelBasedContentsHmap);
		
		
//		List<Content> listOfContents = chartRepo.findByDomainLevel(domain_id, content_level);
//		String domain_name = domainService.getDomainNameByDomainId(domain_id);
//		domainLevelBasedContentsHmap.put(domain_name, listOfContents.size());
		return chartDataClass;
	}
	

	

	//Create Chart Content vd Content extension
	public HashMap<String, Integer> countContentByExtension(int domain_id)
	{
		String[] extensions = {"pdf","jpg","jpeg","txt","xlsx","csv","ppt","docx","doc","pptx","mp4","gif","NA"};
		HashMap<String,Integer> chartDataBasedOnExtension = new HashMap<String, Integer>();
		for(String extension:extensions)
		{
			List<Content> listOfContentsByDomainIdExtension = contentRepo.findContentsByDomainIdExtension(domain_id,extension);
			if(listOfContentsByDomainIdExtension.size()!=0)
			{
				if(extension.equalsIgnoreCase("NA"))
				{
					chartDataBasedOnExtension.put("LINK", listOfContentsByDomainIdExtension.size());
				}
				
				else
				{
					chartDataBasedOnExtension.put(extension, listOfContentsByDomainIdExtension.size());
				}
				
			}
			
		}
		
		System.out.println("Chart data based on extension"+chartDataBasedOnExtension);
		return chartDataBasedOnExtension;
		
	}
	
	
}
