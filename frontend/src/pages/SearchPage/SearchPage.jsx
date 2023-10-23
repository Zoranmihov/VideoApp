import React, { useState, useRef, useCallback, useEffect, useContext } from 'react'
import axios from 'axios';
import "./searchPage.css"
import { delVideo, openEditVideo, closeVideoEdit, editVideo } from './searchPageUtils';

import VideoCard from '../../componnents/videoCard/VideoCard';
import EditIcon from '../../componnents/MenuIcon/EditIcon';
import AppContext from '../../context/AppContext';

export default function SearchPage({ resetSearch }) {
  let { user } = useContext(AppContext);
  let searchTerm = window.location.href.split("/");
  searchTerm = searchTerm[searchTerm.length - 1]

  let [videos, setVideos] = useState([]);
  let [page, setPage] = useState(0);
  let [hasMore, setHasMore] = useState(true);
  let observer = useRef();
  const fetchRef = useRef({});

  const lastVideoRef = useCallback(node => {
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [hasMore]);

  const fetchVideos = useCallback(async () => {
    if (!hasMore) return;

    // If this page has already been fetched, don't fetch it again
    if (fetchRef.current[page]) return;
    fetchRef.current[page] = true;  // Mark this page as fetched

    try {
      const res = await axios.get(`https://localhost:8443/videos/search/${searchTerm}?page=${page}&size=10`, { withCredentials: true });
      setVideos(prevVideos => [...prevVideos, ...res.data.content]);
      setHasMore(!res.data.last);
    } catch (err) {
      console.log(err);
    }
  }, [page, hasMore, searchTerm]);

  useEffect(() => {
    fetchVideos();
  }, [fetchVideos]);

  return (
    <>
      {videos.length > 0 ? (
        <div id="searchPage">
          {videos.map((video, index) => {
            if (videos.length === index + 1) {
              return (
                <>
                  <div className='SearchCard' ref={lastVideoRef} key={index}>
                    <VideoCard videoData={video} />
                    <div>
                      {user.role == "admin" ? (
                        <div className='iconBox'>
                          <p onClick={() => delVideo(video.videoId, video.uploadedBy, user, resetSearch)} className='del'>X</p>
                          <div onClick={() => openEditVideo(video, user, resetSearch)}>
                            <EditIcon />
                          </div>
                        </div>
                      ) : (null)}
                    </div>
                  </div>
                </>
              )
            } else {
              return (
                <div className='SearchCard' ref={lastVideoRef} key={index}>
                  <VideoCard videoData={video} />
                  <div>
                    {user.role == "admin" ? (
                      <div className='iconBox'>
                        <p onClick={() => delVideo(video.videoId, video.uploadedBy, user, resetSearch)} className='del'>X</p>
                        <div onClick={() => openEditVideo(video, user, resetSearch)}>
                          <EditIcon />
                        </div>
                      </div>
                    ) : (null)}
                  </div>
                </div>
              )
            }
          })}
          <div id='editVideoFormModal'>
            <form onSubmit={(e) => {
              editVideo(e, user, resetSearch)
            }} id='editVidForm'>
              <p id='editVidError' className='error'>Error goes here</p>
              <input id="videoTitle" placeholder='Title' type="text" name='title' />
              <textarea id="videoDesc" name="description"></textarea>
              <input id='videoId' className='hiddenInput' type="text" name='videoId' />
              <input id='uploadedBy' className='hiddenInput' type="text" name='uploadedBy' />
              <div id='editVidButtons'>
                <button className='editVidButton' id='editConfirm' type='submit'>Confirm</button>
                <button className='editVidButton' type='button' onClick={() => closeVideoEdit()} >Cancel</button>
              </div>
            </form>
          </div>
        </div>
      ) : (
        <div>No videos found</div>
      )}
    </>
  );
}
