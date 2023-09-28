import React, { useState, useEffect } from 'react';
import './videoPlayer.css';
import { calculateDaysAgo } from '../videoCard/VideoCardUtils';

import axios from 'axios';

export default function VideoPlayer({ videoId, onClose }) {

    const streamUrl = `https://localhost:8443/videos/video/${videoId}`;

    let [description, setDescription] = useState("Lorem ipsum...");

    let [showFullDescription, setShowFullDescription] = useState(false);
    const displayedDescription = showFullDescription ? description : description.substring(0, 100) + "...";

    const toggleDescription = () => {
        setShowFullDescription(!showFullDescription);
    };

    const updateCharCount = (e) => {
        const charCount = document.querySelector("#charCount");
        if (e.target.value.length === 0) {
            charCount.style.display = "none";
        } else {
            charCount.style.display = "block";
            charCount.textContent = e.target.value.length + " of 3000";
        }
    };


    return (
        <div id='videoPlayer'>
            <div id='video'>
                <video controls width="100%" height="100%">
                    <source src={streamUrl} type="video/mp4" />
                    Your browser does not support the video tag.
                </video>
                <button onClick={onClose}>Close</button>
            </div>
            <div className='videoDescription'>
                <p id='description'>
                    {displayedDescription}
                </p>
                {description.length > 100 && (
                    <button
                        onClick={toggleDescription}
                        className="descriptionButton"
                    >
                        {showFullDescription ? "Show Less" : "Show More"}
                    </button>
                )}
            </div>
            <div className='videoInfo'>
                <h2 className='info'>Uploaded by</h2>
                <h2 className='info'>{calculateDaysAgo("2023-09-28 19:33:51.756747")}</h2>
            </div>
            <div id='comments'>
                <form action="">
                    <input onChange={updateCharCount} type="text" placeholder='Leave a comment' name='comment' maxLength={3000} />
                    <p id='charCount'></p>
                    <div id='commentsButtonsContainer'>
                        <button type='submit' className='commentBtn'>Comment</button>
                        <button type='reset' className='commentBtn'>Cancel</button>
                    </div>
                </form>
                <div>
                    <div className='commentBox'>
                        <p className='timestamps'>{calculateDaysAgo("2023-09-28 19:33:51.756747")}</p>
                        <p className='usernames titlesFont'>@Username:</p>
                        <p className='comments'>{description}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
