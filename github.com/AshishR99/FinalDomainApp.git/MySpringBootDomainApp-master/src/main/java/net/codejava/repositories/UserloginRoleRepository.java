package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.codejava.payload.UserloginRolePayload;

public interface UserloginRoleRepository extends JpaRepository<UserloginRolePayload, Integer>
{
	@Query(value = "SELECT userlogin.*,role.role_desc FROM   userlogin LEFT JOIN role ON userlogin.fk_role_id = role.role_id;",nativeQuery = true)
	public List<UserloginRolePayload> findAllUsersWithRoleDesc();

}
