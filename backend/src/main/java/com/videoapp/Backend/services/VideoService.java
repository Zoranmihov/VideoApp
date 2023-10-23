package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.*;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.CommentRepository;
import com.videoapp.Backend.repositories.VideoRepository;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;


@Service
@Transactional
public class VideoService {

 private static final Logger logger = Logger.getLogger(VideoService.class.getName());


 @Autowired
 private VideoRepository videoRepository;

 @Autowired
 CommentRepository commentRepository;



 @Value("${upload.path.base}")
 private String baseUploadPath;

 @Value("${spring.servlet.multipart.location}")
 private String tempUploadPath;

 public ResponseEntity<VideoInfoDTO> getVidInfo(Integer videoId){
  Optional<Video> foundVideo = videoRepository.findById(videoId);
  if (foundVideo.isPresent()){
   Video video = foundVideo.get();
   VideoInfoDTO videoInfoDTO = new VideoInfoDTO(video.getVideoId(), video.getDescription(), video.getTitle(), video.getUploadedBy(), video.getTimestamp());
   return ResponseEntity.ok(videoInfoDTO);
  }
  return ResponseEntity.status(404).body(null);
 }

 public ResponseEntity<Page<GetVideosDTO>> searchVideos(String searchTerm, Integer page, Integer size) {
  Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
  Page<Video> videosPage = videoRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
  Page<GetVideosDTO> dtoPage = videosPage.map(GetVideosDTO::fromVideo);
  return ResponseEntity.ok(dtoPage);
 }

 public List<GetVideosDTO> getRecentVideos() {
  List<Video> videos = videoRepository.findTop20ByOrderByTimestampDesc();

  // Create a list of GetVideosDTO objects from the list of Video objects.
  List<GetVideosDTO> getVideosDTOS = new ArrayList<>();
  for (Video video : videos) {
   // Create a new GetVideosDTO object.
   GetVideosDTO getVideosDTO = new GetVideosDTO(video.getVideoId(), video.getTitle(), video.getDescription(), video.getUploadedBy(), video.getTimestamp(), video.getThumbnail());

   // Add the GetVideosDTO object to the list.
   getVideosDTOS.add(getVideosDTO);
  }

  // Return the list of GetVideosDTO objects.
  return getVideosDTOS;
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

 public VideoStreamDTO streamVideo(Integer videoId, String rangeHeader)
         throws IOException, ExecutionException, InterruptedException {
  // Fetch video details from the database
  Video video = videoRepository.findById(videoId)
          .orElseThrow(() -> new IOException("Video not found"));

  String videoPath = video.getVideoPath();
  File videoFile = new File(videoPath);

  if (!videoFile.exists()) {
   throw new IOException("Video file not found");
  }

  // Parse the Range header to determine the byte range to serve
  long start = 0;
  long end = videoFile.length() - 1;
  if (rangeHeader != null) {
   String[] ranges = rangeHeader.replace("bytes=", "").split("-");
   start = Long.parseLong(ranges[0]);
   if (ranges.length > 1) {
    end = Long.parseLong(ranges[1]);
   }
  }

  // Open the video file for asynchronous reading
  try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(
          Paths.get(videoPath), StandardOpenOption.READ)) {

   ByteBuffer buffer = ByteBuffer.allocate((int) (end - start + 1));

   // Read the chunk from the video file
   Future<Integer> result = afc.read(buffer, start);

   // Wait for the read operation to complete
   int bytesRead = result.get();

   if (bytesRead <= 0) {
    throw new IOException("No bytes read from file");
   }

   buffer.flip();
   byte[] chunk = new byte[bytesRead];
   buffer.get(chunk);

   // Create a ByteArrayResource to hold the chunk of video
   Resource resource = new ByteArrayResource(chunk);

   String contentRange = "bytes " + start + "-" + end + "/" + videoFile.length();
   return new VideoStreamDTO(206, contentRange, resource);
  }
 }

 public ResponseEntity<String> delVideo(DeleteVideoDTO deleteVideoDTO) {
  if (!deleteVideoDTO.getUploadedBy().equals(deleteVideoDTO.getUserName()) || !deleteVideoDTO.getUserRole().equals("admin"))
  {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have permission to do this");
  }
   Optional<Video> video = videoRepository.findById(deleteVideoDTO.getVideoId());
   if(video.isPresent()){
    String videoPathStr = video.get().getVideoPath();  // Assuming getVideoPath() returns the video path as a String
    Path videoPath = Paths.get(videoPathStr).getParent();

    try {
     Files.walk(videoPath)
             .sorted(Comparator.reverseOrder())  // Important to delete contents before the directory itself
             .map(Path::toFile)
             .forEach(File::delete);
    } catch (IOException e) {
     e.printStackTrace();
     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete video directory");
    }

    videoRepository.delete(video.get());
    return ResponseEntity.ok("Done");
   } else {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong or video wasn't found");
   }
 }

 public ResponseEntity<String> editVideo(EditVideoDTO editVideoDTO) {
  if (!editVideoDTO.getUploadedBy().equals(editVideoDTO.getUsername()) || !editVideoDTO.getUserRole().equals("admin"))
  {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have permission to do this");
  }

  Optional<Video> foundVideo = videoRepository.findById(editVideoDTO.getVideoId());
   if(foundVideo.isPresent())
   {
    Video video = foundVideo.get();
    video.setTitle(editVideoDTO.getTitle());
    video.setDescription(editVideoDTO.getDescription());
    videoRepository.save(video);
    return ResponseEntity.ok("Done");
   }
   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video was not found");
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

 private Path convertAndResizeVideo(Path videoPath) throws IOException {
  String inputVideoPath = videoPath.toString();
  String outputVideoPath = inputVideoPath.substring(0, inputVideoPath.lastIndexOf('.')) + ".mp4";

  try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath)) {
   grabber.start();

   int width = grabber.getImageWidth();
   int height = grabber.getImageHeight();
   double frameRate = grabber.getFrameRate();
   int videoBitrate = grabber.getVideoBitrate();

   // Check if resizing is needed
   boolean resizeNeeded = height > 1080;
   if (resizeNeeded) {
    width = (int) (width * (1080.0 / height));
    height = 1080;
   }

   int audioChannels = grabber.getAudioChannels();
   int audioBitrate = grabber.getAudioBitrate();
   int sampleRate = grabber.getSampleRate();

   // Check if conversion is needed
   boolean conversionNeeded = !inputVideoPath.endsWith(".mp4") || resizeNeeded;

   if (conversionNeeded) {
    try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, width, height)) {
     recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);  // Set H264 codec for both .mov and .mp4
     recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);  // Set AAC codec for both .mov and .mp4

     recorder.setVideoOption("profile", "high");
     recorder.setFormat("mp4");
     recorder.setFrameRate(frameRate);
     recorder.setVideoBitrate(videoBitrate);
     recorder.setImageWidth(width);
     recorder.setImageHeight(height);
     recorder.setAudioChannels(audioChannels);
     recorder.setAudioBitrate(audioBitrate);
     recorder.setSampleRate(sampleRate);

     recorder.start();

     Frame frame;
     while ((frame = grabber.grab()) != null) {
      recorder.record(frame);
     }
    }
   } else {
    // If no conversion or resizing is needed, just return the original path
    return videoPath;
   }

  } catch (Exception e) {
   throw new IOException("Error during video conversion: " + e.getMessage(), e);
  }

  // If the input video path and output video path are different, delete the original file
  if (!inputVideoPath.equals(outputVideoPath)) {
   Files.delete(videoPath);
  }

  return Paths.get(outputVideoPath);
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