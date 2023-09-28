import React, {useState} from 'react'
import { calculateDaysAgo } from './VideoCardUtils'
import "./videoCard.css"

import VideoPlayer from '../videoPlayer/VideoPlayer';

export default function VideoCard({ videoData }) {
  const [showVideoPlayer, setShowVideoPlayer] = useState(false);

  const handleThumbnailClick = () => {
    setShowVideoPlayer(true);
  };

  const handleCloseVideoPlayer = () => {
    setShowVideoPlayer(false);
  };

  return (
    <div className='videoCard'>
      <div onClick={handleThumbnailClick}>
        <img
          src={`data:image/jpeg;base64,${videoData.thumbnail}`}
          alt="Video Thumbnail"
          loading="lazy"
        />
      </div>
      <div id='infoContainer'>
        <h2 className='titlesFont' onClick={handleThumbnailClick}>{videoData.title}</h2>
        <div className='vidInfo'>
          <h3>{videoData.uploadedBy}</h3>
          <h3>{calculateDaysAgo(videoData.uploadedAt)}</h3>
        </div>
      </div>
      {showVideoPlayer && <VideoPlayer videoId={videoData.videoId} onClose={handleCloseVideoPlayer} />}
    </div>
  )
}
