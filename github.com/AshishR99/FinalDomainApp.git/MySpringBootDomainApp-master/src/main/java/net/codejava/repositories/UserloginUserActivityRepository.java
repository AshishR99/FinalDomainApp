package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.UserActivity;
import net.codejava.payload.UserloginUserActivityPayload;

public interface UserloginUserActivityRepository extends JpaRepository<UserloginUserActivityPayload,Integer>{
	
	@Query(value = "select user_activity.`*`,userlogin.user_name from user_activity LEFT JOIN userlogin on user_activity.fk_user_id = userlogin.user_id ",nativeQuery = true)
	public List<UserloginUserActivityPayload> findAllUsersActivityDetailsWithUsernames();
	
	@Query(value = "select user_activity.`*`,userlogin.user_name from user_activity LEFT JOIN userlogin on user_activity.fk_user_id = userlogin.user_id where fk_user_id=?1",nativeQuery = true)
	public List<UserloginUserActivityPayload> findUserActivityByUserId(int user_id);
}
