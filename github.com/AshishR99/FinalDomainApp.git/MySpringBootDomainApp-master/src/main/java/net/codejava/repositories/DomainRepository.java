package net.codejava.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
import net.codejava.entities.Domain;
import net.codejava.entities.Subdomain;

public interface DomainRepository extends JpaRepository<Domain, Integer>{

	public Boolean existsByname(String name);
	
	public Boolean existsBytags(String tagName);
}
