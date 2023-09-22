import React, { useEffect, useState } from 'react'
import './index1.css'
import axios from 'axios'

import VideoCard from '../../componnents/videoCard/VideoCard'
import Loading from '../Loading/Loading'


export default function Index() {
  let [videos, setVideos] = useState(false)
  useEffect(() => {
    axios.get("https://localhost:8443/videos/all").then(res => {
     setVideos(res.data)
    }).catch(err => console.log(err))
  }, [])

  return (
    <>
    {videos ? (
          <div id='index'>
          {
          videos.map(video => {
            return <VideoCard key={video.id} videoData={video} />
          })}
    
        </div>
    ) : (<Loading />)}
    </>
  )
}
