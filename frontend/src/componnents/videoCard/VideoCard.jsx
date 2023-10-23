import React, { useContext, useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { calculateDaysAgo } from './VideoCardUtils'
import "./videoCard.css"

import AppContext from '../../context/AppContext';


export default function VideoCard({ videoData }) {
  let { setVideoDetails } = useContext(AppContext);
  const navigate = useNavigate();
  const [thumbnailSrc, setThumbnailSrc] = useState('');

  useEffect(() => {
    const detectImageType = (bytes) => {
      // Check the start of the buffer for common image file signatures
      if (bytes.length < 4) {
        return null; // Not enough data to determine type
      }
      if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
        return 'image/jpeg';
      } else if (bytes[0] === 0x89 && bytes[1] === 0x50 && bytes[2] === 0x4E && bytes[3] === 0x47) {
        return 'image/png';
      }
      return null; // Unknown type or not an image
    };

    // Function to convert the thumbnail data to a Blob
    const thumbnailToBlob = (thumbnailData) => {
      // 1. Parse the numbers from the thumbnail data
      const byteNumbers = thumbnailData.split(',').map(num => parseInt(num, 10));
  
      // 2. Convert the array of byte values into a Uint8Array
      const byteArray = new Uint8Array(byteNumbers);
  
      // 3. Detect the image type (JPEG or PNG) based on the byte signature
      const imageType = detectImageType(byteArray);
  
      // 4. Create a blob from the byte array with the correct MIME type
      return new Blob([byteArray], { type: imageType });
    };
  
    // Function to convert Blob to Base64 data URL
    const readAsDataURL = (blob) => {
      const reader = new FileReader();
      reader.onloadend = () => {
        setThumbnailSrc(reader.result); // The Base64 string data URL
      };
      reader.readAsDataURL(blob);
    };
  
    // Check if thumbnail data is available, then process it
    if (videoData.thumbnail) {
      const imageBlob = thumbnailToBlob(videoData.thumbnail);
      readAsDataURL(imageBlob);
    }
  }, [videoData.thumbnail]);

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
