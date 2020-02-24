package com.example.demo;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.codejava.entities.Role;
import net.codejava.repositories.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBoot2ApplicationTests {
	
	@Autowired
	RoleRepository roleRepo;

	@Test
	public void contextLoads() {
//		Role role = new net.codejava.entities.Role(4,"Sales Person");
//		roleRepo.save(role);
				
	}
	
	

}
