package net.codejava.payload;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserloginUserActivityPayload {
	
	@Id
	private int user_activity_sl_no;
	private int fk_user_id;
	private String login_time;
	private String logout_time;
	private String loggedin_activity_time;
	private String user_name;
	
	public int getUser_activity_sl_no() {
		return user_activity_sl_no;
	}
	public void setUser_activity_sl_no(int user_activity_sl_no) {
		this.user_activity_sl_no = user_activity_sl_no;
	}
	public int getFk_user_id() {
		return fk_user_id;
	}
	public void setFk_user_id(int fk_user_id) {
		this.fk_user_id = fk_user_id;
	}
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public String getLogout_time() {
		return logout_time;
	}
	public void setLogout_time(String logout_time) {
		this.logout_time = logout_time;
	}
	public String getLoggedin_activity_time() {
		return loggedin_activity_time;
	}
	public void setLoggedin_activity_time(String loggedin_activity_time) {
		this.loggedin_activity_time = loggedin_activity_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "UserloginUserActivityPayload [user_activity_sl_no=" + user_activity_sl_no + ", fk_user_id=" + fk_user_id
				+ ", login_time=" + login_time + ", logout_time=" + logout_time + ", loggedin_activity_time="
				+ loggedin_activity_time + ", user_name=" + user_name + "]";
	}

}
