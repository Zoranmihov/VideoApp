package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.GetHomeVideosDTO;
import com.videoapp.Backend.dto.VideoUploadDTO;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.VideoRepository;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;


@Service
@Transactional
public class VideoService {

 private static final Logger logger = Logger.getLogger(VideoService.class.getName());


 @Autowired
 private VideoRepository videoRepository;

 @Value("${upload.path.base}")
 private String baseUploadPath;

 @Value("${spring.servlet.multipart.location}")
 private String tempUploadPath;

 public List<GetHomeVideosDTO> getRecentVideos() {
  List<Video> videos = videoRepository.findTop20ByOrderByTimestampDesc();

  // Create a list of GetHomeVideosDTO objects from the list of Video objects.
  List<GetHomeVideosDTO> getHomeVideosDTOs = new ArrayList<>();
  for (Video video : videos) {
   // Create a new GetHomeVideosDTO object.
   GetHomeVideosDTO getHomeVideosDTO = new GetHomeVideosDTO(video.getVideoId(), video.getTitle(), video.getUploadedBy(), video.getTimestamp(), video.getThumbnail());

   // Add the GetHomeVideosDTO object to the list.
   getHomeVideosDTOs.add(getHomeVideosDTO);
  }

  // Return the list of GetHomeVideosDTO objects.
  return getHomeVideosDTOs;
 }

 @Async
 public Future<String> videoUpload(VideoUploadDTO videoUploadDTO, String username) {
  try {
   // Replace whitespaces in the username
   username = username.replaceAll("\\s+", "_");

   // Create a directory for the user in the base upload path
   Path userDirectory = Paths.get(baseUploadPath, username);

   // Replace whitespaces in the video title
   String videoDirectoryName = videoUploadDTO.getTitle().replaceAll("\\s+", "_");
   String originalDirectoryName = videoDirectoryName;
   int count = 1;
   while (Files.exists(userDirectory.resolve(videoDirectoryName))) {
    videoDirectoryName = originalDirectoryName + "_(" + count + ")";
    count++;
   }

   Path videoDirectory = userDirectory.resolve(videoDirectoryName);
   Files.createDirectories(videoDirectory);

   // Get the video and thumbnail paths from the DTO
   String videoPath = videoUploadDTO.getVideoTempPath();

   // Check if the video file exists
   if (!Files.exists(Paths.get(videoPath))) {
    throw new IOException("Video file not found: " + videoPath);
   }

   // Move the video file to the video directory
   Files.move(Paths.get(videoPath), videoDirectory.resolve(Paths.get(videoPath).getFileName()));

   // Convert the video to mp4 format
   Path convertedVideoPath = convertAndResizeVideo(videoDirectory.resolve(Paths.get(videoPath).getFileName()));

   if (videoUploadDTO.getThumbnail() == null) {
    videoUploadDTO.setThumbnail(extractThumbnailBytes(convertedVideoPath));
   }

   // Create a new Video object and save it to the database
   Video video = new Video(videoUploadDTO.getTitle(), videoUploadDTO.getDescription(), convertedVideoPath.toString(), videoUploadDTO.getThumbnail(), username, LocalDateTime.now());
   videoRepository.save(video);

   // Clear temp directory
   clearTempDirectory(username);

   return new AsyncResult<>("Video uploaded successfully");
  } catch (Exception e) {
   logger.severe("Error during video upload for user: " + username + ". Error: \n" + e);
   throw new RuntimeException("Video upload failed. Please check logs for more details.", e);
  }
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

 public byte[] extractThumbnailBytes(Path convertedVideoPath) throws IOException {
  FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(convertedVideoPath.toString());
  grabber.start();

  // Grab the first frame
  Frame frame = grabber.grabImage();

  // Use JavaCV's FrameToImage to convert the Frame to a BufferedImage
  Java2DFrameConverter converter = new Java2DFrameConverter();
  BufferedImage thumbnailImage = converter.convert(frame);

  // Use Java's ImageIO to write the BufferedImage to a ByteArrayOutputStream
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  ImageIO.write(thumbnailImage, "jpg", baos);
  byte[] thumbnailBytes = baos.toByteArray();

  grabber.stop();

  return thumbnailBytes;
 }


 private void clearTempDirectory(String username) {
  Path absoluteTempDirectory = Paths.get(tempUploadPath).toAbsolutePath();
  Path userTempDirectory = absoluteTempDirectory.resolve(username);

  try (DirectoryStream<Path> stream = Files.newDirectoryStream(userTempDirectory)) {
   for (Path path : stream) {
    Files.deleteIfExists(path);
   }
  } catch (IOException e) {
   System.err.println("Failed to clear temp directory: " + e.getMessage());
  }

  try {
   Files.deleteIfExists(userTempDirectory);
  } catch (IOException e) {
   System.err.println("Failed to delete user temp directory: " + e.getMessage());
  }
 }
}