package net.codejava.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.Role;
import net.codejava.repositories.RoleRepository;

@Service
@Transactional
public class RoleService {

	// Role services
	@Autowired
	private RoleRepository roleRepo;

	boolean status = false;


	public List<Role> listAll() {
		return roleRepo.findAll();
	}

	public boolean save(Role role) {

		
		try
		{
			roleRepo.save(role);
			status = true;
		}

		catch(Exception ex)
		{
			status = false;
		}

		return status;
	}


	public Role get(int id) {
		return roleRepo.findById(id).get();
	}

	public void delete(int id) {
		roleRepo.deleteById(id);
	}
	

}
