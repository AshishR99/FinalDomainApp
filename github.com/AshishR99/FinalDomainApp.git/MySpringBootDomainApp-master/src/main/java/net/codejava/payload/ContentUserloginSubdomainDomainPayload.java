package net.codejava.payload;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContentUserloginSubdomainDomainPayload {

	@Id
	private int content_id;
	private int fk_user_id;
	private int fk_domain_id;
	private int fk_subdomain_id;
	private String content_level;
	private String content_tags;
	private String content_type;
	private String content_size;
	private String content_URL;
	private String content_desc;
	private String content_name;
	private String uploading_date_time;
	private String content_actual_path;
	private String content_extension;
	private String user_name;
	private String subdomainName;
	private String name;
	
	public int getContent_id() {
		return content_id;
	}
	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}
	public int getFk_user_id() {
		return fk_user_id;
	}
	public void setFk_user_id(int fk_user_id) {
		this.fk_user_id = fk_user_id;
	}
	public int getFk_domain_id() {
		return fk_domain_id;
	}
	public void setFk_domain_id(int fk_domain_id) {
		this.fk_domain_id = fk_domain_id;
	}
	public int getFk_subdomain_id() {
		return fk_subdomain_id;
	}
	public void setFk_subdomain_id(int fk_subdomain_id) {
		this.fk_subdomain_id = fk_subdomain_id;
	}
	public String getContent_level() {
		return content_level;
	}
	public void setContent_level(String content_level) {
		this.content_level = content_level;
	}
	public String getContent_tags() {
		return content_tags;
	}
	public void setContent_tags(String content_tags) {
		this.content_tags = content_tags;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getContent_size() {
		return content_size;
	}
	public void setContent_size(String content_size) {
		this.content_size = content_size;
	}
	public String getContent_URL() {
		return content_URL;
	}
	public void setContent_URL(String content_URL) {
		this.content_URL = content_URL;
	}
	public String getContent_desc() {
		return content_desc;
	}
	public void setContent_desc(String content_desc) {
		this.content_desc = content_desc;
	}
	public String getContent_name() {
		return content_name;
	}
	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}
	public String getUploading_date_time() {
		return uploading_date_time;
	}
	public void setUploading_date_time(String uploading_date_time) {
		this.uploading_date_time = uploading_date_time;
	}
	public String getContent_actual_path() {
		return content_actual_path;
	}
	public void setContent_actual_path(String content_actual_path) {
		this.content_actual_path = content_actual_path;
	}
	public String getContent_extension() {
		return content_extension;
	}
	public void setContent_extension(String content_extension) {
		this.content_extension = content_extension;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getSubdomainName() {
		return subdomainName;
	}
	public void setSubdomainName(String subdomainName) {
		this.subdomainName = subdomainName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ContentUserloginSubdomainDomainPayload [content_id=" + content_id + ", fk_user_id=" + fk_user_id
				+ ", fk_domain_id=" + fk_domain_id + ", fk_subdomain_id=" + fk_subdomain_id + ", content_level="
				+ content_level + ", content_tags=" + content_tags + ", content_type=" + content_type
				+ ", content_size=" + content_size + ", content_URL=" + content_URL + ", content_desc=" + content_desc
				+ ", content_name=" + content_name + ", uploading_date_time=" + uploading_date_time
				+ ", content_actual_path=" + content_actual_path + ", content_extension=" + content_extension
				+ ", user_name=" + user_name + ", subdomainName=" + subdomainName + ", name=" + name + "]";
	}
}
