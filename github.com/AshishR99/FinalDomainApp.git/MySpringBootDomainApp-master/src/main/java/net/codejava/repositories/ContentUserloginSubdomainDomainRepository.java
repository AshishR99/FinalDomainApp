package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.codejava.entities.Content;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;

public interface ContentUserloginSubdomainDomainRepository extends JpaRepository<ContentUserloginSubdomainDomainPayload, Integer>{

	@Query(value = "select content.*,userlogin.user_name,subdomain.subdomain_name,domain.name from content left join userlogin on userlogin.user_id = content.fk_user_id left join subdomain on subdomain.subdomain_id = content.fk_subdomain_id left join domain on domain.id = content.fk_domain_id where content.fk_domain_id = ?1",nativeQuery = true)
	public List<ContentUserloginSubdomainDomainPayload> getContentsByDomainId(int fk_domain_id);
	
	@Query(value = "select content.*,userlogin.user_name,subdomain.subdomain_name,domain.name from content left join userlogin on userlogin.user_id = content.fk_user_id left join subdomain on subdomain.subdomain_id = content.fk_subdomain_id left join domain on domain.id = content.fk_domain_id where content.content_id = ?1",nativeQuery = true)
	public ContentUserloginSubdomainDomainPayload getContentsByContentId(int content_id);
	
	@Query(value = "select content.*,userlogin.user_name,subdomain.subdomain_name,domain.name from content left join userlogin on userlogin.user_id = content.fk_user_id left join subdomain on subdomain.subdomain_id = content.fk_subdomain_id left join domain on domain.id = content.fk_domain_id where content.fk_user_id = ?1",nativeQuery = true)
	public List<ContentUserloginSubdomainDomainPayload> getContentsByUserId(int fk_user_id);
	
	
	@Query(value="select content.*,userlogin.user_name,subdomain.subdomain_name,domain.name from content left join userlogin on userlogin.user_id = content.fk_user_id left join subdomain on subdomain.subdomain_id = content.fk_subdomain_id left join domain on domain.id = content.fk_domain_id",nativeQuery = true)
	public List<ContentUserloginSubdomainDomainPayload> getAllContentsOfUsers();
	
}
