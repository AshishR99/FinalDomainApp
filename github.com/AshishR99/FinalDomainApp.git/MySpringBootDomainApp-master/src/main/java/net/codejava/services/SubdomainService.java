package net.codejava.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.Domain;
import net.codejava.entities.Subdomain;
import net.codejava.payload.DomainSubdomainPayload;
import net.codejava.repositories.DomainSubdomainRepository;
import net.codejava.repositories.SubdomainRepository;

@Service
@Transactional
public class SubdomainService {

	
	@Autowired
	private SubdomainRepository subdomainRepo;
	
	@Autowired
	private DomainSubdomainRepository domainSubdomainRepo;
	
	boolean status = false;
	
	public List<Subdomain> listAll() {
        return subdomainRepo.findAll();
    }
     
    public boolean save(Subdomain subdomain) 
    {
  
    	try
    	{
    		subdomainRepo.save(subdomain);
    		status = true;
    	}
    	
    	catch(Exception ex)
    	{
    	   status = false;	
    	}
    	return status;
    	
    }
     
    public Subdomain get(int id) {
        return subdomainRepo.findById(id).get();
    }
     
    public void delete(int id) {
    	subdomainRepo.deleteById(id);
    }

    //Load Subdomains with Domain names
    public List<DomainSubdomainPayload> listSubdomainsWithDomainNames()
    {
    	List<DomainSubdomainPayload> listOfSubdomainsWithDomains = domainSubdomainRepo.getAllSubdomainsWithDomains();
    	return listOfSubdomainsWithDomains;
    }
    
    //Returning list of subdomains using domain id
    public List<Subdomain> loadSubDomainsByDomainId(int fk_domain_id)
    {
    	return subdomainRepo.findByDomainId(fk_domain_id);
    	
    }
    
    //get Subdomain name by subdomain id
    public String getSubdomainNameBySubdomainId(int subdomain_id)
    {
    	Subdomain subdomainobj = subdomainRepo.findById(subdomain_id).get();
    	return subdomainobj.getSubdomainName();
    }
    
    //GetSubdomains by Domain ID
    public boolean getSubdomainsByDomainId(int fk_domain_id)
    {
    	
    //	String errorMessage = "";
    	boolean isHavingSubdomain = false;
    	try
    	{
    		List<Subdomain> subdomainListByDomainId = subdomainRepo.findByDomainId(fk_domain_id);
    		if(!subdomainListByDomainId.isEmpty())
    		{
    			isHavingSubdomain = true;
    		}
    	}
    	
    	catch(Exception ex)
    	{
    		isHavingSubdomain=false;
    	}
    	
    	return isHavingSubdomain;
    }
    
}
