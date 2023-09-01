package com.videoapp.Backend;

import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.models.Role;
import com.videoapp.Backend.repositories.RoleRepository;
import com.videoapp.Backend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			if(userRepository.findByUsername("superAdmin").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			ApplicationUser admin = new ApplicationUser(1, "superAdmin", passwordEncoder.encode("password"), roles);

			userRepository.save(admin);
		};
	}
}
