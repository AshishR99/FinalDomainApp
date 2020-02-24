package net.codejava.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.Domain;
import net.codejava.entities.Subdomain;
import net.codejava.repositories.DomainRepository;
import net.codejava.repositories.SubdomainRepository;


@Service
@Transactional
public class DomainService {

	// Domain services
	@Autowired
    private DomainRepository domainRepo;
	
	
     
    public List<Domain> listAll() {
        return domainRepo.findAll();
    }
     
    public boolean save(Domain domain) {
    	
    //	domain = domainRepo.save(domain);
    	
    	boolean status = false;
    	try
    	{
    		domainRepo.save(domain);
    		status = true;
    		
    	}
    	
    	catch(Exception ex)
    	{
    	   status = false;	
    	}
    	return status;
    }
     
    public Domain get(int id) {
        return domainRepo.findById(id).get();
    }
     
    public boolean delete(int id) {
    	boolean status = false;
    	try
    	{
    		domainRepo.deleteById(id);
    		status = true;
    	}
    	
    	catch(Exception ex)
    	{
    		status =false;
    	}
    	
    	return status;
    }
    
    public String getDomainNameByDomainId(int domain_id)
    {
    	Domain domainobj = domainRepo.findById(domain_id).get();
    	return domainobj.getName();
    }
    
    public String getTagsByDomainId(int domain_id)
    {
    	Domain domainobj = domainRepo.findById(domain_id).get();
    	return domainobj.getTags();
    }
    
    
    /* 
    @Transactional
    public void addSubdomain(Domain domain)
    {
    	
    	List<Domain> list = domainRepo.findAll();
    	for(int i=0;i<list.size();i++)
    	{
    		Domain domainObj = (Domain)list.get(i);
    		int domain_id = domainObj.getId();
    		String domain_name= domainObj.getName();
    		//System.out.println("domain id:"+domain_id+"domain name:"+domain_name);
    	}
    	
    	
    	
    	
    	for(int i=0;i<list.size();i++)
    	{
    		Domain domainObj = (Domain)list.get(i);
    		int domain_id = domainObj.getId();
    		String domain_name= domainObj.getName();
    		System.out.println("domain id:"+domain_id+"domain name:"+domain_name);
    	}
    }*/
}
