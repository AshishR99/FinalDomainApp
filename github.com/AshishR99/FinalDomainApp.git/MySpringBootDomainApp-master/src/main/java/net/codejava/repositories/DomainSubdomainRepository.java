package net.codejava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.codejava.payload.DomainSubdomainPayload;

public interface DomainSubdomainRepository extends JpaRepository<DomainSubdomainPayload, Integer>{

	@Query(value = "SELECT subdomain.*,domain.name FROM  subdomain LEFT JOIN domain ON subdomain.fk_domain_id = domain.id;",nativeQuery = true)
	public List<DomainSubdomainPayload> getAllSubdomainsWithDomains();
}
