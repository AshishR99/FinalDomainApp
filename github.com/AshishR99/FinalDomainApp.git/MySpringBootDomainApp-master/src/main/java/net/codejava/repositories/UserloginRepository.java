package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.Userlogin;
import net.codejava.payload.UserloginRolePayload;

@Repository
public interface UserloginRepository extends JpaRepository<Userlogin, Integer>{

	@Query(value="select * from userlogin where user_email=?1", nativeQuery = true)
	public Userlogin findByUserEmail(String user_email);
	
	@Query(value="select * from userlogin where user_email=?1 and user_password=?2", nativeQuery = true)
	public Userlogin findByCredentials(String user_email, String user_pwd);
	
	@Query(value="select * from userlogin where fk_role_id=?1", nativeQuery = true)
	public List<Userlogin> find_by_role_id(int role_id);
	
	@Query(value = "select * from userlogin where created_by=?1",nativeQuery = true)
	public List<Userlogin> searchCreatorByUserId(int user_id);
	
	@Query(value = "select fk_domain_id from userlogin where user_id=?1",nativeQuery = true)
	public String findDomainIdByUserId(int user_id);
	
	@Query(value = "select fk_role_id from userlogin where user_id=?1",nativeQuery = true)
	public int findRoleIdByUserId(int user_id);
}
