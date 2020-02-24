package net.codejava.payload;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DomainSubdomainPayload {

	@Id
	private int subdomainId;
	private String subdomainName;
	private int FK_domain_id;
	private String name;
	
	public int getSubdomainId() {
		return subdomainId;
	}
	public void setSubdomainId(int subdomainId) {
		this.subdomainId = subdomainId;
	}
	public String getSubdomainName() {
		return subdomainName;
	}
	public void setSubdomainName(String subdomainName) {
		this.subdomainName = subdomainName;
	}
	public int getFK_domain_id() {
		return FK_domain_id;
	}
	public void setFK_domain_id(int fK_domain_id) {
		FK_domain_id = fK_domain_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "DomainSubdomainPayload [subdomainId=" + subdomainId + ", subdomainName=" + subdomainName
				+ ", FK_domain_id=" + FK_domain_id + ", name=" + name + "]";
	}
}
