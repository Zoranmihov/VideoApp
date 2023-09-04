import React from 'react'
import "./videoCard.css"

export default function VideoCard({ title, img, uploaded_by, uploaded_date }) {
  return (
    <div id='videoCard'>
      <div>
        <img src="./Thumbnail.webp" alt="Video Thumbnail" loading="lazy" />
      </div>
      <div id='infoContainer'>
        <h2>Video Title</h2>
        <div id='vidInfo'>
          <h3>UploadedByUser</h3>
          <h3>UploadedAgo</h3>
        </div>
      </div>
    </div>
  )
}