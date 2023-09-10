package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.VideoUploadDTO;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

@Service
@Transactional
public class VideoService {

 @Autowired
 private VideoRepository videoRepository;

 @Value("${upload.path.base}")
 private String baseUploadPath;

 @Value("${spring.servlet.multipart.location}")
 private String tempUploadPath;

 @Async
 public Future<String> videoUpload(VideoUploadDTO videoUploadDTO, String username) throws IOException {
  // Replace whitespaces in the username
  username = username.replaceAll("\\s+", "_");

  Path userDirectory = Paths.get(baseUploadPath, username);

  // Replace whitespaces in the video title
  String videoDirectoryName = videoUploadDTO.getTitle().replaceAll("\\s+", "_");
  String originalDirectoryName = videoDirectoryName;
  int count = 1;
  while (Files.exists(userDirectory.resolve(videoDirectoryName))) {
   videoDirectoryName = originalDirectoryName + " (" + count + ")";
   count++;
  }

  Path videoDirectory = userDirectory.resolve(videoDirectoryName);
  Files.createDirectories(videoDirectory);

  String originalVideoName = videoUploadDTO.getVideo().getOriginalFilename();
  String originalThumbnailName = videoUploadDTO.getThumbnail().getOriginalFilename();

  System.out.println("Attempting to move video to: " + videoDirectory.resolve(originalVideoName).toString());

  Path tempVideoPath = Paths.get(tempUploadPath, originalVideoName);
  if (!Files.exists(tempVideoPath)) {
   throw new IOException("Temporary video file not found: " + tempVideoPath.toString());
  }
  Files.move(tempVideoPath, videoDirectory.resolve(originalVideoName));

  Path tempThumbnailPath = Paths.get(tempUploadPath, originalThumbnailName);
  if (!Files.exists(tempThumbnailPath)) {
   throw new IOException("Temporary thumbnail file not found: " + tempThumbnailPath.toString());
  }
  Files.move(tempThumbnailPath, videoDirectory.resolve(originalThumbnailName));

  Video video = new Video(videoUploadDTO.getTitle(), videoUploadDTO.getDescription(), videoDirectory.resolve(originalVideoName).toString(), videoDirectory.resolve(originalThumbnailName).toString(), username, LocalDateTime.now());
  videoRepository.save(video);

  clearTempDirectory();

  return new AsyncResult<String>("Video uploaded successfully");
 }

 private void clearTempDirectory() {
  // Convert the relative path to an absolute path
  Path absoluteTempDirectory = Paths.get(tempUploadPath).toAbsolutePath();
  try (DirectoryStream<Path> stream = Files.newDirectoryStream(absoluteTempDirectory)) {
   for (Path path : stream) {
    Files.deleteIfExists(path);
   }
  } catch (IOException e) {
   System.err.println("Failed to clear temp directory: " + e.getMessage());
  }
 }

}
