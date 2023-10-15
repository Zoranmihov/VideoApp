import React, { useState, useEffect, useRef, useCallback } from 'react'
import './comment.css'

import axios from 'axios';
import { calculateDaysAgo } from '../videoCard/VideoCardUtils';
import { delComment, updateCharCount, updateEditCharCount, postComment, openEdit, closeEdit , editComment} from './CommentUtils';
import EditIcon from '../MenuIcon/EditIcon';

export default function Comment({ videoId, videoUploader, user, resetComments}) {

    let [comments, setComments] = useState([]);
    let [page, setPage] = useState(0);
    let [hasMore, setHasMore] = useState(true);
    let observer = useRef();
    const fetchRef = useRef({});

    const lastCommentRef = useCallback(node => {
        if (observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                setPage(prevPage => prevPage + 1);
            }
        });
        if (node) observer.current.observe(node);
    }, [hasMore]);

    const fetchComments = useCallback(async () => {
        if (!hasMore) return;

        // If this page has already been fetched, don't fetch it again
        if (fetchRef.current[page]) return;
        fetchRef.current[page] = true;  // Mark this page as fetched

        try {
            const res = await axios.get(`https://localhost:8443/videos/getcomments/${videoId}?page=${page}&size=10`, { withCredentials: true });
            setComments(prevComments => [...prevComments, ...res.data.content]);
            setHasMore(!res.data.last);
        } catch (err) {
            console.log(err);
        }
    }, [page, hasMore]);

    useEffect(() => {
        fetchComments();
    }, [fetchComments]);


    return (
        <div id='comments'>
            <form onSubmit={(e) => postComment(e, videoId, user.username, resetComments)} >
                <p id='commentError' className='error'>Error goes here</p>
                <input onChange={updateCharCount} type="text" placeholder='Leave a comment' name='comment' required maxLength={2000} />
                <p id='charCount'></p>
                <div id='commentsButtonsContainer'>
                    <button disabled id='subComment' type='submit' className='commentBtn'>Comment</button>
                    <button type='reset' className='commentBtn'>Cancel</button>
                </div>
            </form>
            <div>
                <div>
                    {comments.map((comment, index) => (
                        <div className='commentBox' ref={index === comments.length - 1 ? lastCommentRef : null} key={comment.commentId}>
                            <div>
                                <p className='timestamps'>{calculateDaysAgo(comment.timestamp)}</p>
                                <p className='usernames titlesFont'>@{comment.commentedBy}:</p>
                                <p className='comments'>{comment.content}</p>
                            </div>
                            {user.username == videoUploader || user.role == "admin" ? (
                                <div className='iconBox'>
                                    <p onClick={() => delComment(comment, user, resetComments)} className='del'>X</p>
                                    <div onClick={() => openEdit(comment)}>
                                        <EditIcon />
                                    </div>
                                </div>
                            ) : (null)}
                        </div>
                    ))}
                </div>
            </div>

            <div id='editModel'>
                <form onSubmit={(e) => {
                    editComment(e, user, resetComments)
                }} id='editForm'>
                    <p id='editError' className='error'>Error goes here</p>
                    <input placeholder='Edit the comment' id='editComment' type="text" name='edit' required maxLength={2000} onChange={(e) => updateEditCharCount(e)} />
                    <p id='editCharCount'></p>
                    <input id='commentIdInput' type="text" name='commentId' />
                    <input id='commentBy' type="text" name='commentBy' />
                    <div id='editButtons'>
                        <button className='editButton' id='editConfirm' disabled type='submit'>Confirm</button>
                        <button className='editButton' type='button' onClick={() => closeEdit()} >Cancel</button>
                    </div>
                </form>
            </div>
        </div>

    )
}
