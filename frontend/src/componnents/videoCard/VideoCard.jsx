import React, { useContext } from 'react'
import { useNavigate } from 'react-router-dom';
import { calculateDaysAgo } from './VideoCardUtils'
import "./videoCard.css"

import AppContext from '../../context/AppContext';


export default function VideoCard({ videoData }) {
  let { setVideoDetails } = useContext(AppContext);
  const navigate = useNavigate();

  const handleClick = () => {
    setVideoDetails({
      id: videoData.videoId,
      description: videoData.videoDescription,
      title: videoData.title,
      uploadedBy: videoData.uploadedBy,
      uploadedAt: videoData.uploadedAt
    });
   navigate("/videoplayer/" + videoData.videoId);;
  }

  return (
    <div onClick={() => handleClick()} className='videoCard'>
      <div>
        <img
          src={`data:image/jpeg;base64,${videoData.thumbnail}`}
          alt="Video Thumbnail"
          loading="lazy"
        />
      </div>
      <div id='infoContainer'>
        <h2 className='titlesFont'>{videoData.title}</h2>
        <div className='vidInfo'>
          <h3>{videoData.uploadedBy}</h3>
          <h3>{calculateDaysAgo(videoData.uploadedAt)}</h3>
        </div>
      </div>
    </div>
  )
}
