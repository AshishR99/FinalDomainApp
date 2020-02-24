package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.Content;


@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>{
	
	@Query(value = "select * from content where fk_subdomain_id=?1",nativeQuery = true)
	public List<Content> findContentDetailsBySubdomainId(int fk_subdomain_id);
	
	
	@Query(value = "select * from content where fk_user_id=?1",nativeQuery = true)
	public List<Content> findContentsByUserId(int fk_user_id);
	
	
	@Query(value = "select * from content where fk_domain_id=?1 and content_extension=?2",nativeQuery = true)
	public List<Content> findContentsByDomainIdExtension(int fk_domain_id,String content_extension);
	
}

