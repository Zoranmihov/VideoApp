import React, { useEffect, useState, useContext } from 'react'
import { useNavigate } from "react-router-dom";
import './index1.css'
import axios from 'axios'

import VideoCard from '../../componnents/videoCard/VideoCard'
import Loading from '../Loading/Loading'
import AppContext from '../../context/AppContext';

export default function Index() {
  let { setVideoDetails } = useContext(AppContext);
  let [videos, setVideos] = useState(false)
  const navigate = useNavigate();


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
            videos.map((video, index) => {
              return <div onClick={() => navigate("/videoplayer/" + video.videoId)}> <VideoCard key={index} videoData={video} /> </div>
            })}

        </div>
      ) : (<Loading />)}
    </>
  )
}
