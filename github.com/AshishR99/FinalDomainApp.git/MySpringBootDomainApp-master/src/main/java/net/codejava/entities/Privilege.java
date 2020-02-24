package net.codejava.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Privilege {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int functionality_id;
	private String functionality_desc;
	private int fk_role_id;
	
	public int getFunctionality_id() {
		return functionality_id;
	}
	public void setFunctionality_id(int functionality_id) {
		this.functionality_id = functionality_id;
	}
	public String getFunctionality_desc() {
		return functionality_desc;
	}
	public void setFunctionality_desc(String functionality_desc) {
		this.functionality_desc = functionality_desc;
	}
	public int getFk_role_id() {
		return fk_role_id;
	}
	public void setFk_role_id(int fk_role_id) {
		this.fk_role_id = fk_role_id;
	}
	@Override
	public String toString() {
		return "Privilege [functionality_id=" + functionality_id + ", functionality_desc=" + functionality_desc
				+ ", fk_role_id=" + fk_role_id + "]";
	}
	

}
