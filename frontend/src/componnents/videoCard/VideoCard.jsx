import React from 'react'
import { calculateDaysAgo } from './VideoCardUtils'
import "./videoCard.css"

export default function VideoCard({ videoData }) {

  return (
    <div className='videoCard'>
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