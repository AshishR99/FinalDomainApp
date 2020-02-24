package net.codejava.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Domain {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String tags;
	private String status;
	private int created_by;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	@Override
	public String toString() {
		return "Domain [id=" + id + ", name=" + name + ", tags=" + tags + ", status=" + status + ", created_by="
				+ created_by + "]";
	}
	public Domain(int id, String name, String tags, String status, int created_by) {
		super();
		this.id = id;
		this.name = name;
		this.tags = tags;
		this.status = status;
		this.created_by = created_by;
	}
	public Domain() {
		super();
	}
	
	
	
}
