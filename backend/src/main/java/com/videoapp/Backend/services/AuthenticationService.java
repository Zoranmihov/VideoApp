package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.models.Role;
import com.videoapp.Backend.repositories.RoleRepository;
import com.videoapp.Backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public boolean registerUser(String username, String password){
        try {
            Optional<Role> optionalRole = roleRepository.findByAuthority("USER");
            if (!optionalRole.isPresent()) {
                // Log the error or handle the case where the role is not found
                return false;
            }
            Role userRole = optionalRole.get();
            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);
            userRepository.save(new ApplicationUser(0, username, passwordEncoder.encode(password), authorities));
            return true;
        } catch (DataAccessException dae) {
            // Log database-related exceptions
            // log.error("Database error occurred", dae);
            return false;
        } catch (Exception exception) {
            // Log other exceptions
            // log.error("An error occurred", exception);
            return false;
        }
    }

    public LoginResponseDTO loginUser(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException ex) {
            return new LoginResponseDTO(null, "failed");
        }
    }

}
