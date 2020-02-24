package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.Content;

@Repository
public interface ChartRepository extends JpaRepository<Content, Integer>{

	@Query(value = "select * from content where fk_domain_id=?1 and content_level=?2",nativeQuery = true)
	public List<Content> findByDomainLevel(int domain_id, String content_level);
	
	@Query(value = "select * from content where fk_domain_id=?1",nativeQuery = true)
	public List<Content> findByDomainId(int domain_id);
}
