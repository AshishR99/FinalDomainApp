package net.codejava.payload;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserloginRolePayload {

	@Id
	private int user_id;
	private String user_email;
	private String user_name;
	private String user_password;
	private String user_phone;
	private int fk_role_id;
	private String fk_domain_id;
	private String role_desc;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public int getFk_role_id() {
		return fk_role_id;
	}
	public void setFk_role_id(int fk_role_id) {
		this.fk_role_id = fk_role_id;
	}
	public String getFk_domain_id() {
		return fk_domain_id;
	}
	public void setFk_domain_id(String fk_domain_id) {
		this.fk_domain_id = fk_domain_id;
	}
	public String getRole_desc() {
		return role_desc;
	}
	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}
	@Override
	public String toString() {
		return "userloginRolePayload [user_id=" + user_id + ", user_email=" + user_email + ", user_name=" + user_name
				+ ", user_password=" + user_password + ", user_phone=" + user_phone + ", fk_role_id=" + fk_role_id
				+ ", fk_domain_id=" + fk_domain_id + ", role_desc=" + role_desc + "]";
	}
	
	
}
