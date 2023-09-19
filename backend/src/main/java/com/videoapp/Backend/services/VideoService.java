package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.VideoUploadDTO;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.VideoRepository;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
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

  // Convert the video to mp4 format
  Path convertedVideoPath = convertAndResizeVideo(videoDirectory.resolve(originalVideoName));

  // Create a new Video object and save it to the database
  Video video = new Video(videoUploadDTO.getTitle(), videoUploadDTO.getDescription(), convertedVideoPath.toString(), videoDirectory.resolve(originalThumbnailName).toString(), username, LocalDateTime.now());
  videoRepository.save(video);

  clearTempDirectory();

  return new AsyncResult<>("Video uploaded successfully");
 }

 private Path convertAndResizeVideo(Path videoPath) throws IOException {
  String inputVideoPath = videoPath.toString();

  // Check if the input video is either .mov or .mp4
  if (!inputVideoPath.endsWith(".mov") && !inputVideoPath.endsWith(".mp4")) {
   throw new IllegalArgumentException("Unsupported video format. Only .mov and .mp4 are accepted.");
  }

  // Set the output format to .mp4
  String outputVideoPath = inputVideoPath.substring(0, inputVideoPath.lastIndexOf('.')) + ".mp4";

  try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath)) {
   grabber.start();

   int width = grabber.getImageWidth();
   int height = grabber.getImageHeight();

   // If height is greater than 1080p, resize while maintaining aspect ratio
   if (height > 1080) {
    width = (int) (width * (1080.0 / height));
    height = 1080;
   }

   try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, width, height)) {
    recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
    recorder.setFormat("mp4");
    recorder.setFrameRate(grabber.getFrameRate());
    recorder.setVideoBitrate(grabber.getVideoBitrate());
    recorder.setImageWidth(width);
    recorder.setImageHeight(height);

    recorder.start();

    Frame frame;
    while ((frame = grabber.grab()) != null) {
     recorder.record(frame);
    }
   }
  } catch (Exception e) {
   throw new IOException("Error during video conversion", e);
  }

  // Delete the original video if it's different from the output
  if (!inputVideoPath.equals(outputVideoPath)) {
   Files.delete(videoPath);
  }

  return Paths.get(outputVideoPath);
 }

 private void clearTempDirectory() {
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