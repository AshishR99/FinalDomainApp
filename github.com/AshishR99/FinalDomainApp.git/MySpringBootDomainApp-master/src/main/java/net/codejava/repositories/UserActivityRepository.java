package net.codejava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.codejava.entities.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Integer>{

	
	@Query(value = "select * from user_activity where fk_user_id=?1 and logout_time=?2 and loggedin_activity_time=?3",nativeQuery = true)
	public UserActivity findUserLastActivityData(int user_id, String logout_time, String user_activity_time);
	
	
}
