package net.codejava.repositories;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.codejava.entities.Userlogin;

@Repository
public interface UserloginRepository extends JpaRepository<Userlogin, Integer>{

	@Query(value="select * from userlogin where user_email=?1", nativeQuery = true)
	public Userlogin findByUserEmail(String userEmail);
	
	@Query(value="select * from userlogin where user_email=?1 and user_password=?2", nativeQuery = true)
	public Userlogin findByCredentials(String userEmail, String user_pwd);
	
	@Query(value="select * from userlogin where fk_role_id=?1", nativeQuery = true)
	public List<Userlogin> find_by_role_id(int role_id);
	
	@Query(value = "select * from userlogin where created_by=?1",nativeQuery = true)
	public List<Userlogin> searchCreatorByUserId(int user_id);
	
	@Query(value = "select fk_domain_id from userlogin where user_id=?1",nativeQuery = true)
	public String findDomainIdByUserId(int user_id);
	
	@Query(value = "select fk_role_id from userlogin where user_id=?1",nativeQuery = true)
	public int findRoleIdByUserId(int user_id);
	
    @Modifying
	@Query(value= "UPDATE  userlogin set user_password=?1 where user_id=?2",nativeQuery = true)	
	public int updatePassword(String password, int userId);
	
Optional<Userlogin> findByuserEmail(String userEmail);
	
	Optional<Userlogin> findByuserId(Long userId);
	
	Boolean existsByuserEmail(String userEmail);
	
	

	//List<User> findByRole(String roleName);
}

	
