package net.codejava.entities;

import java.util.HashMap;

public class ChartDataBasedOnLevel {

	private String domain_name;
	private HashMap<String, Integer> domainContentsHashmap;
	public String getDomain_name() {
		return domain_name;
	}
	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}
	public HashMap<String, Integer> getDomainContentsHashmap() {
		return domainContentsHashmap;
	}
	public void setDomainContentsHashmap(HashMap<String, Integer> domainContentsHashmap) {
		this.domainContentsHashmap = domainContentsHashmap;
	}
	public ChartDataBasedOnLevel(String domain_name, HashMap<String, Integer> domainContentsHashmap) {
		super();
		this.domain_name = domain_name;
		this.domainContentsHashmap = domainContentsHashmap;
	}
	public ChartDataBasedOnLevel() {
		super();
	}
	

	
}
