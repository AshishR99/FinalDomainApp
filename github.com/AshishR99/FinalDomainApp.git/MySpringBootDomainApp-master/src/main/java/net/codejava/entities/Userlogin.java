package net.codejava.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Userlogin {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userEmail;
	private String user_name;	
	private String user_password;
	private String user_phone;
	private int fk_role_id;
	private String fk_domain_id;
	private String salt_value;
	private int created_by;
	
	
	public int getUser_id() {
		return userId;
	}
	public void setUser_id(int user_id) {
		this.userId = user_id;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public String getSalt_value() {
		return salt_value;
	}
	public void setSalt_value(String salt_value) {
		this.salt_value = salt_value;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	@Override
	public String toString() {
		return "Userlogin [user_id=" + userId + ", userEmail=" + userEmail + ", user_name=" + user_name
				+ ", user_password=" + user_password + ", user_phone=" + user_phone + ", fk_role_id=" + fk_role_id
				+ ", fk_domain_id=" + fk_domain_id + ", salt_value=" + salt_value + ", created_by=" + created_by + "]";
	}
	public Userlogin(int user_id, String userEmail, String user_name, String user_password, String user_phone,
			int fk_role_id, String fk_domain_id, String salt_value, int created_by) {
		super();
		this.userId = user_id;
		this.userEmail = userEmail;
		this.user_name = user_name;
		this.user_password = user_password;
		this.user_phone = user_phone;
		this.fk_role_id = fk_role_id;
		this.fk_domain_id = fk_domain_id;
		this.salt_value = salt_value;
		this.created_by = created_by;
	}
	public Userlogin() {
		super();
	}
	
	
			
}
