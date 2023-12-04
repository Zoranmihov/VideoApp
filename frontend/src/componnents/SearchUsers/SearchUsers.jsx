import React, { useState, useRef, useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./searchUsers.css";
import axios from 'axios';

import Avatar from '../Avatar/Avatar';

export default function SearchUsers({ searchTerm }) {
    const navigate = useNavigate();
    let [users, setUsers] = useState([]);
    let [page, setPage] = useState(0);
    let [hasMore, setHasMore] = useState(true);
    let observer = useRef();
    const fetchRef = useRef({});

    const lastUserRef = useCallback(node => {
        if (observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                setPage(prevPage => prevPage + 1);
            }
        });
        if (node) observer.current.observe(node);
    }, [hasMore]);

    const fetchUsers = useCallback(async () => {
        if (!hasMore) return;

        if (fetchRef.current[page]) return;
        fetchRef.current[page] = true;

        try {
            const res = await axios.get(`https://localhost:8443/user/search/${searchTerm}?page=${page}&size=10`, { withCredentials: true });
            setUsers(prevUsers => [...prevUsers, ...res.data.content]);
            setHasMore(!res.data.last);
        } catch (err) {
            console.log(err);
        }
    }, [page, hasMore, searchTerm]);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    useEffect(() => {
        return () => {
            if (observer.current) observer.current.disconnect();
        };
    }, []);

    return (
        <div id='searchUsers'>
            {users.length > 0 ? (
                users.map((user, index) => (
                    <div onClick={() => navigate("/user/" + user.username)} key={index} className='userCard' ref={index === users.length - 1 ? lastUserRef : null}>
                        {user.avatar ? (
                            <div className='imgContainer'>
                                <img
                                    src={`data:image/jpeg;base64,${user.avatar}`}
                                    alt="User Avatar"
                                    loading="lazy"
                                />
                            </div>
                        ) : (
                            <div className='imgContainer'>
                                <Avatar />
                            </div>
                        )}
                        <h2 className='titlesFont userCard-username'>{user.username}</h2>
                    </div>
                ))
            ) : (null)}
        </div>
    );
}
