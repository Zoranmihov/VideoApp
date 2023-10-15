import React, { useState, useEffect, useContext} from 'react';
import './videoPlayer.css';
import { calculateDaysAgo } from '../../componnents/videoCard/VideoCardUtils';

import Loading from '../Loading/Loading';
import Comment from '../../componnents/Comment/Comment';

import axios from 'axios';
import AppContext from '../../context/AppContext';

export default function VideoPlayer() {
    let { videoDetails, setVideoDetails, user } = useContext(AppContext);
    let [loading, setLoading] = useState(false);
    let [streamUrl, setStreamUrl] = useState("");
    let [showFullDescription, setShowFullDescription] = useState(false);
    const [commentKey, setCommentKey] = useState(0);


    useEffect(() => {
        let videoId = window.location.href.split("/");
        videoId = videoId[videoId.length - 1]
        axios.get(`https://localhost:8443/videos/video/${videoId}`).then(async (res) => {
            setStreamUrl(res.config.url);
            if (Object.keys(videoDetails).length === 0) {
                let videoData = await axios.get(`https://localhost:8443/videos/video/info/${videoId}`);
                setVideoDetails(videoData.data);
            }
            setLoading(true)
        }).catch(err => {
            console.log(err)
            setLoading(true)
        })
    }, [])


    const displayedDescription = showFullDescription || !videoDetails.description || videoDetails.description.length <= 100 ?
        videoDetails.description :
        videoDetails.description.substring(0, 100) + "...";

    const toggleDescription = () => {
        setShowFullDescription(!showFullDescription);
    };

    const resetComments = () => {
        setCommentKey(prevKey => prevKey + 1);
    };

    return (
        <>
            {loading ? (
                streamUrl ? (
                    <div id='videoPlayer'>
                        <div id='video'>
                            <video controls width="100%" height="100%">
                                <source src={streamUrl} type="video/mp4" />
                                Your browser does not support the video tag.
                            </video>
                        </div>
                        <div className='videoDescription'>
                            <p id='description'>
                                {displayedDescription}
                            </p>
                            {videoDetails.description && videoDetails.description.length > 100 && (
                                <button
                                    onClick={toggleDescription}
                                    className="descriptionButton"
                                >
                                    {showFullDescription ? "Show Less" : "Show More"}
                                </button>
                            )}
                        </div>
                        <div className='videoInfo'>
                            <h2 className='info'>Uploaded by: {videoDetails.uploadedBy}</h2>
                            <h2 className='info'>{calculateDaysAgo(videoDetails.uploadedAt)}</h2>
                        </div>
                        <Comment  resetComments={resetComments} key={commentKey} videoUploader={videoDetails.uploadedBy} user={user} videoId={videoDetails.id} />
                    </div>
                ) : (
                    <h1>Video was not found or deleted</h1>
                )
            ) : (
                <Loading />
            )}
        </>
    );

}
