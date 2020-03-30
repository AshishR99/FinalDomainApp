package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.Content;
import net.codejava.payload.ContentUserloginSubdomainDomainPayload;



@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>{
	
	@Query(value = "select * from content where fk_subdomain_id=?1",nativeQuery = true)
	public List<Content> findContentDetailsBySubdomainId(int fk_subdomain_id);
	
	
	@Query(value = "select * from content where fk_user_id=?1",nativeQuery = true)
	public List<Content> findContentsByUserId(int fk_user_id);
	
	
	@Query(value = "select * from content where fk_domain_id=?1 and content_extension=?2",nativeQuery = true)
	public List<Content> findContentsByDomainIdExtension(int fk_domain_id,String content_extension);
	
	
	@Query(value = "SELECT * FROM  content LEFT JOIN role ON content.content_id = content.content_id;",nativeQuery = true)
	public List<Content> findAllContents();
	
	
}

