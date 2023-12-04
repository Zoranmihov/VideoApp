import React, { useState, useRef, useCallback, useEffect } from 'react';
import "./userPage.css"

import axios from 'axios';

import VideoCard from '../../componnents/videoCard/VideoCard';
import Avatar from '../../componnents/Avatar/Avatar';

export default function UserPage() {

  let [userInfo, setUserInfo] = useState({});
  let [videos, setVideos] = useState([]);
  let [page, setPage] = useState(0);
  let [hasMore, setHasMore] = useState(true);
  let observer = useRef();
  const fetchRef = useRef({});

  let searchedUser = window.location.href.split("/");
  searchedUser = searchedUser[searchedUser.length - 1]

  const fetchUserInfo = useCallback(async () => {
    if (!hasMore) return;

    if (fetchRef.current[page]) return;
    fetchRef.current[page] = true;

    try {
      const res = await axios.get(`https://localhost:8443/user/profile/${searchedUser}?page=${page}&size=10`, { withCredentials: true });
      setUserInfo({
        username: res.data.username,
        avatar: res.data.avatar
      })
      setVideos(prevVideos => [...prevVideos, ...res.data.videos.content]);
      setHasMore(!res.data.last);
    } catch (err) {
      console.log(err);
    }
  }, [page, hasMore, searchedUser]);

  const lastVideoRef = useCallback(node => {
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [hasMore]);


  useEffect(() => {
    fetchUserInfo();
  }, [fetchUserInfo]);

  return (
    <div id='userPage'>
      <div id='userPageInfo'>
        {userInfo.avatar ? (
          <div className='imgContainer'>
            <img
              src={`data:image/jpeg;base64,${userInfo.avatar}`}
              alt="User Avatar"
              loading="lazy"
            />
          </div>
        ) : (
          <div className='imgContainer'>
            <Avatar />
          </div>
        )}
        <h2 className='titlesFont userPage-username'>{userInfo.username}</h2>
      </div>
      <div id='hr'>
          <h2>Videos :</h2>
          <hr />
      </div>
      <div id='userPageVideos'>
        {videos && videos.length > 0 ? (
          videos.map((video, index) => {
            if (videos.length === index + 1) {
              // The last video card gets the ref for infinite scrolling
              return <div className='userPageVideoCard' ref={lastVideoRef} key={video.id}><VideoCard videoData={video} /></div>
            } else {
              return <div className='userPageVideoCard' key={video.id}><VideoCard videoData={video} /></div>
            }
          })
        ) : (
          <p>No videos to display</p>
        )}
      </div>
    </div>
  )
}
