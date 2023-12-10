import React, { useState, useEffect, useRef, useCallback } from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import "./userVideos.css"

import { closeEditButtons, openEditButtons, openForm, resetVideoEditForm, updateThumbnailPicker, updateFileName, subVideoEditForm, checkTitle, checkDescription } from "./UserVideosUtils"

import VideoCard from '../videoCard/VideoCard';
import EditIcon from '../MenuIcon/EditIcon';

export default function UserVideos({ username, resetVideos }) {
  const navigate = useNavigate();
  const [videos, setVideos] = useState([]);
  const [videoId, setVideoId] = useState();
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const observer = useRef();
  const fetchRef = useRef({});

  // Fetch videos
  const fetchVideos = useCallback(async () => {
    if (!hasMore || fetchRef.current[page]) return;

    fetchRef.current[page] = true; // Mark this page as fetched

    try {
      const res = await axios.get(`https://localhost:8443/user/allvideos/${username}`, {
        withCredentials: true
      });
      setVideos(prevVideos => [...prevVideos, ...res.data.content]);
      setHasMore(!res.data.last);
    } catch (error) {
      console.error('Failed to fetch videos:', error);
    }
  }, [username, hasMore]); // Removed 'page' from dependencies

  // Setup intersection observer
  const lastVideoRef = useCallback(node => {
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [hasMore]);

  // Fetch videos on mount
  useEffect(() => {
    fetchVideos();
  }, []); // Empty dependency array for initial fetch

  // Fetch videos when page changes
  useEffect(() => {
    fetchVideos();
  }, [fetchVideos]);



  return (
    <>
      {
        videos.length > 0 ? (
          <div className="userVideos">
            {videos.map((video, index) => (
              <div onClick={() => navigate("/videoplayer/" + video.videoId)} className='userVideosCardContainer' key={index}>
                <div className='videoCard' ref={videos.length === index + 1 ? lastVideoRef : null}>
                  <VideoCard videoData={video} />
                </div>
                <div className='iconBox'>
                  <div onClick={() => {
                    setVideoId(video.videoId)
                    openEditButtons()
                  }}>
                    <EditIcon />
                  </div>
                  <p className='del' onClick={() => {
                    setVideoId(video.videoId)
                    openForm("delete")
                  }}>X</p>
                </div>
              </div>
            ))}

            <div id='editUserVideos'>
              <div className='editVidInfoBtns'>
                <button onClick={() => openForm("title")}>Change Title</button>
                <button onClick={() => openForm("description")}>Change Description</button>
              </div>
              <div className='editVidInfoBtns'>
                <button onClick={() => openForm("thumbnail")}>Change Thumbnail</button>
                <button onClick={() => { closeEditButtons() }}>Cancel</button>
              </div>
            </div>

            <div id="updateVideoInfoModal">
              <form id='updateProfileForm' onSubmit={(e) => subVideoEditForm(e, username, videoId, resetVideos)} onReset={(e) => resetVideoEditForm()} >
                <h2 id='updateVideoTitle' className='titlesFont'>Title goes here</h2>
                <label className='updateLabel' id='vidTextBasedLabel'>Test</label>
                <p className='thumbnailPicker' id='editThumbnailName'></p>
                <p className='error' id='updateVidError'></p>
                <input onInput={(e) => checkTitle(e)} id='videoTextUpdates' type="text" name='textBasedUpdates' />
                <textarea onInput={(e) => checkDescription(e)} name="descriptionUpdate" id="descriptionUpdateText"></textarea>
                <input onChange={(e) => updateFileName(e)} id='changeThumbnailPicker' type="file" name='thumbnailUpdates' />
                <input type="text" name="targetPicked" id="videoTargetPicked" />
                <button className='thumbnailPicker' id='avatarPickerBtn' type='button' onClick={() => updateThumbnailPicker()}>Chose a photo</button>
                <div className='changeButtonContainer'>
                  <button disabled id='ChangeVidInfoSubBtn' type='submit'>Update</button>
                  <button type='reset'>Cancel</button>
                </div>
              </form>
            </div>
          </div>
        ) : (
          <div className='noResults'> <h2>You have no videos</h2></div>
        )
      }
    </>
  );
}

