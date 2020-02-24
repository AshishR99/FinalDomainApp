package net.codejava.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int role_id;
	private String role_desc;
	private int created_by;
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getRole_desc() {
		return role_desc;
	}
	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	@Override
	public String toString() {
		return "Role [role_id=" + role_id + ", role_desc=" + role_desc + ", created_by=" + created_by + "]";
	}
	public Role(int role_id, String role_desc, int created_by) {
		super();
		this.role_id = role_id;
		this.role_desc = role_desc;
		this.created_by = created_by;
	}
	public Role() {
		super();
	}
	
	
	

}
