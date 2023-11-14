package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.EditUserInfoDTO;
import com.videoapp.Backend.dto.UserInfoDTO;
import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.models.Comment;
import com.videoapp.Backend.models.Role;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.CommentRepository;
import com.videoapp.Backend.repositories.UserRepository;
import com.videoapp.Backend.repositories.VideoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${upload.path.base}")
    private String baseUploadPath;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }

        public ResponseEntity<UserInfoDTO> editUserInfo(EditUserInfoDTO editUserInfoDTO, HttpServletResponse response){
            ApplicationUser user = userRepository.findByUsername(editUserInfoDTO.getUsername()).get();
            // Check if passwords match before fetching the rest of the user data
            if(!passwordEncoder.matches(editUserInfoDTO.getPassword(), user.getPassword())) {
                return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(null, null, null, null, "Invalid password"), HttpStatus.BAD_REQUEST);
            }

            Set<Role> roles = (Set<Role>) user.getAuthorities();
            Optional<Role> roleOptional = roles.stream().findFirst();
            String role =  roleOptional.get().getAuthority();



            switch (editUserInfoDTO.getTarget()){
                case "username":
                    if(userRepository.existsByUsername(editUserInfoDTO.getData())){
                        return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(null, null, null, null, "Username is taken"), HttpStatus.BAD_REQUEST);

                    }
                        user.setUsername(editUserInfoDTO.getData());
                    userRepository.save(user);
                    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), editUserInfoDTO.getPassword()));
                    String token = tokenService.generateJwt(auth);
                    response.setHeader("Set-Cookie", "JWT=" + token + "; Domain=localhost; Max-Age=86400; Path=/; SameSite=None; Secure; HttpOnly");

                    return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(user.getUsername(), role, token, user.getAvatar(), "Username was updated"), HttpStatus.OK);


                case "password":
                    user.setPassword(passwordEncoder.encode(editUserInfoDTO.getData()));
                    userRepository.save(user);
                    return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(user.getUsername(), role, null, user.getAvatar(), "Password was updated"), HttpStatus.OK);


                case "avatar":
                    byte[] newAvatar = resizeAvatar(editUserInfoDTO.getAvatarBytes(), editUserInfoDTO.getAvatar());
                    user.setAvatar(newAvatar);
                    userRepository.save(user);
                    return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(user.getUsername(), role, null, user.getAvatar(), "Avatar was updated"), HttpStatus.OK);


                case "delete":
                    userRepository.delete(user);
                    clearData(user.getUsername());
                    response.setHeader("Set-Cookie", "JWT=" + null + "; Domain=localhost; Max-Age=1; Path=/; SameSite=None; Secure; HttpOnly");
                    return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(null, role, null, null, "Account was updated"), HttpStatus.NO_CONTENT);

                default:
                    return new ResponseEntity<UserInfoDTO>(new UserInfoDTO(null, null, null, null, "Something went wrong please try again"), HttpStatus.BAD_REQUEST);
            }

        }

    private byte[] resizeAvatar(byte[] avatarBytes, MultipartFile image){
            try {
                // Convert byte array to BufferedImage
                ByteArrayInputStream bais = new ByteArrayInputStream(avatarBytes);
                BufferedImage avatarImage = ImageIO.read(bais);

                // Check for null
                if (avatarImage == null) {
                    System.out.println("Failed to read image");
                    return null;
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
                Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(Files.probeContentType(Paths.get(image.getOriginalFilename())));
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

                return resizedAvatarBytes;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

    }

    @Async
    private void clearData(String username) {

        // Might preserve them in a deleted table for like 30 days

        List<Video> foundVideos = videoRepository.findByUploadedBy(username);
        videoRepository.deleteAll(foundVideos);

        List<Comment> foundComments = commentRepository.findBycommentedBy(username);
        commentRepository.deleteAll(foundComments);

        // Delete the path
        try {
            Path directoryPath = Paths.get(baseUploadPath, username);
            Files.walk(directoryPath)
                    .sorted(Comparator.reverseOrder())  // Important to delete contents before the directory itself
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
