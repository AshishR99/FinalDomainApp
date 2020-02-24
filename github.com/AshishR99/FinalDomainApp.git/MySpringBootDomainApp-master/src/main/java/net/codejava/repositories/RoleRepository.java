package net.codejava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.codejava.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{


}
