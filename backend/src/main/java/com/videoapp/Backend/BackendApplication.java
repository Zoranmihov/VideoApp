package com.videoapp.Backend;

import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.models.Role;
import com.videoapp.Backend.repositories.RoleRepository;
import com.videoapp.Backend.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableAsync(proxyTargetClass=true)
public class BackendApplication {

	@Value("${upload.path.base}")
	private String baseUploadPath;

	@Value("${spring.servlet.multipart.location}")
	private String tempUploadPath;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Create the Video_Uploads directory if it doesn't exist
		File baseUploadDirectory = new File(baseUploadPath);
		if (!baseUploadDirectory.exists()) {
			baseUploadDirectory.mkdirs();
		}

		// Create the temp directory if it doesn't exist
		File tempDirectory = new File(tempUploadPath);
		if (!tempDirectory.exists()) {
			tempDirectory.mkdirs();
		}
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("superAdmin").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			ApplicationUser admin = new ApplicationUser(1, "superAdmin", passwordEncoder.encode("password"), roles, null);

			userRepository.save(admin);
		};
	}
}
