import React, { useState, useContext, useEffect } from 'react'
import './userProfile.css'
import { toggleWindow, updateAvatarPicker, resetEditForm, openForm, subEditForm, updateFileName } from './UserProfileUtils';

import AppContext from '../../context/AppContext';
import Avatar from '../../componnents/Avatar/Avatar';
import UserVideos from '../../componnents/UserVideos/UserVideos';

export default function UserProfile() {
    let { user, setUser } = useContext(AppContext);
    let [toggle, setToggle] = useState(true);

    const [videosKey, setVideosKey] = useState(0);

  const resetVideos = () => {
    setVideosKey(prevKey => prevKey + 1);
};

    useEffect(() => {
        const setAvatarSrc = (base64String) => {
            document.getElementById('avatarProfilePic').src = `data:image/jpeg;base64,${base64String}`;
        };

        if (user.avatar) {
            setAvatarSrc(user.avatar);
        }
    }, [user.avatar]);

    return (
        <>
            <div id='subNav'>
                <p className='profileNav titlesFont navActive' onClick={(e) => toggleWindow(e, setToggle, true)}>Profile</p>
                <p className='profileNav titlesFont' onClick={(e) => toggleWindow(e, setToggle, false)}>Videos</p>
            </div>
            {toggle ? (
                <div id='userProfile'>
                    <div className='userInfo'>
                        {user.avatar ? (
                            <img
                                src=""
                                alt="User Avatar"
                                loading="lazy"
                                id='avatarProfilePic'
                            />
                        ) : (<Avatar width={"250px"} height={"200px"} />)}
                        <h2 id='userName' className='titlesFont'>{user.username}</h2>
                        <div className="changeButtonContainer">
                            <button className='changeButton' onClick={() => openForm("username")}>Change Username</button>
                            <button className='changeButton' onClick={() => openForm("password")}>Change Password</button>
                        </div>
                        <div className="changeButtonContainer">
                            <button className='changeButton' onClick={() => openForm("avatar")}>Change Avatar</button>
                            <button className='changeButton' onClick={() => openForm("delete")}>Delete Profile</button>
                        </div>
                    </div>

                    <div id="updateProfileModal">
                        <form id='updateProfileForm' onSubmit={(e) => subEditForm(e, user.username, user.role, setUser)} onReset={e => resetEditForm()} >
                            <h2 id='updateProfileTitle' className='titlesFont'>Title goes here</h2>
                            <label className='updateLabel' id='textBasedLabel'>Test</label>
                            <p className='avatarPicker' id='editAvatarName'></p>
                            <p className='error' id='updateError'></p>
                            <input id='textUpdates' type="text" name='textBasedUpdates' />
                            <input onChange={(e) => updateFileName(e)} id='changeAvatarPicker' type="file" name='avatarUpdates' />
                            <input type="text" name="targetPicked" id="targetPicked" />
                            <button className='avatarPicker' id='avatarPickerBtn' type='button' onClick={() => updateAvatarPicker()}>Chose a photo</button>
                            <label className='updateLabel' htmlFor="cPassword">Password:</label>
                            <input type="password" name='cPassword' required />
                            <div className='changeButtonContainer'>
                                <button disabled id='ChangeSubBtn' type='submit'>Update</button>
                                <button type='reset'>Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            ) : (
                <div>
                    < UserVideos key={videosKey} resetVideos={resetVideos} username={user.username} />
                </div>
            )}
        </>
    )
}
