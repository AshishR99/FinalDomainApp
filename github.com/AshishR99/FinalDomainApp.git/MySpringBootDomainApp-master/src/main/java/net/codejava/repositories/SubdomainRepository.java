package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.codejava.entities.Subdomain;

public interface SubdomainRepository extends JpaRepository<Subdomain,Integer>{


	@Query(value = "select * from subdomain where fk_domain_id=?1",nativeQuery = true)
	public List<Subdomain> findByDomainId(int fk_domain_id);
}
