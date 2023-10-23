package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.dto.RegistrationDTO;
import com.videoapp.Backend.dto.UserInfoDTO;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
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

    public String registerUser(RegistrationDTO registrationDTO) {
        try {
            Optional<ApplicationUser> existingUser = userRepository.findByUsername(registrationDTO.getUsername());
            if (existingUser.isPresent()) {
                return "Username taken";
            }

            Optional<Role> optionalRole = roleRepository.findByAuthority("USER");
            Role userRole = optionalRole.get();
            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            if(registrationDTO.getAvatarBytes() != null){
                try {
                    // Convert byte array to BufferedImage
                    ByteArrayInputStream bais = new ByteArrayInputStream(registrationDTO.getAvatarFile().getBytes());
                    BufferedImage avatarImage = ImageIO.read(bais);

                    // Check for null
                    if (avatarImage == null) {
                        System.out.println("Failed to read image");
                        return "Error occurred";
                    }

                    // Create a new BufferedImage object to store the result
                    BufferedImage resizedAvatarImage = new BufferedImage(300, 300, avatarImage.getType());

                    // Obtain a Graphics2D object
                    Graphics2D g2d = resizedAvatarImage.createGraphics();

                    // Perform the resize operation
                    g2d.drawImage(avatarImage, 0, 0, 300, 300, null);

                    // Dispose of the Graphics2D object
                    g2d.dispose();

                    // Determine image format (png or jpg)
                    String formatName = null;
                    Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(Files.probeContentType(Paths.get(registrationDTO.getAvatarFile().getOriginalFilename())));
                    if (iter.hasNext()) {
                        ImageReader reader = iter.next();
                        formatName = reader.getFormatName();
                    }

                    // If the format is unrecognized, default to png
                    if (formatName == null || (!formatName.equalsIgnoreCase("png") && !formatName.equalsIgnoreCase("jpeg"))) {
                        formatName = "png";
                    }

                    // Convert BufferedImage back to byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedAvatarImage, formatName, baos);
                    byte[] resizedAvatarBytes = baos.toByteArray();

                    // Debug print
                    System.out.println("Resized avatar bytes size: " + resizedAvatarBytes.length);

                    registrationDTO.setAvatarBytes(resizedAvatarBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error occurred";  // Or handle error accordingly
                }
            }


            userRepository.save(new ApplicationUser(0, registrationDTO.getUsername(), passwordEncoder.encode(registrationDTO.getPassword()), authorities,registrationDTO.getAvatarBytes()));
            return "Success";

        } catch (DataAccessException dae) {

            return "Database error";
        } catch (Exception exception) {

            return "Error occurred";
        }
    }

    public UserInfoDTO loginUser(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);
            ApplicationUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Set<Role> roles = (Set<Role>) user.getAuthorities();
            Optional<Role> roleOptional = roles.stream().findFirst();
            String role = roleOptional.isPresent() ? roleOptional.get().getAuthority() : "UNKNOWN_ROLE";
            return new UserInfoDTO(user.getUsername(),role, token, user.getAvatar(), null);
        } catch (AuthenticationException ex) {
            return new UserInfoDTO("","","Failed", null, null);
        }
    }

    public UserInfoDTO userInfo(String username){
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<Role> roles = (Set<Role>) user.getAuthorities();
        Optional<Role> roleOptional = roles.stream().findFirst();
        String role = roleOptional.isPresent() ? roleOptional.get().getAuthority() : "UNKNOWN_ROLE";
        return new UserInfoDTO(user.getUsername(), role.toLowerCase(), null, user.getAvatar(), null);

    }

}
