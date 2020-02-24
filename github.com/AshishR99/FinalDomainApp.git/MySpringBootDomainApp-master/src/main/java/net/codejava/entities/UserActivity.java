package net.codejava.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_activity_sl_no;
	private int fk_user_id;
	private String login_time;
	private String logout_time;
	private String loggedin_activity_time;
	
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
	
	public UserActivity() {
		super();
	}
	@Override
	public String toString() {
		return "UserActivity [user_activity_sl_no=" + user_activity_sl_no + ", fk_user_id=" + fk_user_id
				+ ", login_time=" + login_time + ", logout_time=" + logout_time + ", loggedin_activity_time="
				+ loggedin_activity_time + "]";
	}
	public UserActivity(int user_activity_sl_no, int fk_user_id, String login_time, String logout_time,
			String loggedin_activity_time) {
		super();
		this.user_activity_sl_no = user_activity_sl_no;
		this.fk_user_id = fk_user_id;
		this.login_time = login_time;
		this.logout_time = logout_time;
		this.loggedin_activity_time = loggedin_activity_time;
	}
}
