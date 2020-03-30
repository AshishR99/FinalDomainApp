package net.codejava.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;	
import javax.persistence.ManyToOne;

@Entity
public class Subdomain {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int subdomainId;
	private String subdomainName;
	private int FK_domain_id;
	private int created_by;
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
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	@Override
	public String toString() {
		return "Subdomain [subdomainId=" + subdomainId + ", subdomainName=" + subdomainName + ", FK_domain_id="
				+ FK_domain_id + ", created_by=" + created_by + "]";
	}
	public Subdomain(int subdomainId, String subdomainName, int fK_domain_id, int created_by) {
		super();
		this.subdomainId = subdomainId;
		this.subdomainName = subdomainName;
		FK_domain_id = fK_domain_id;
		this.created_by = created_by;
	}
	public Subdomain() {
		super();
	}
	
	
	
}
