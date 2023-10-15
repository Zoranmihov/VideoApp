import React, { useState } from 'react';
import "./nav.css";

import avatar from '../../assets/avatar.jpg'

// Components
import Login from '../login/Login';
import Register from '../register/Register';
import UserMenu from '../UserMenu/UserMenu';
import SearchIcon from '../SearchIcon/SearchIcon';

export default function Nav({user, setUser}) {

  const [formToggle, setFormToggle] = useState(false);
  const [modalKey, setModalKey] = useState(0);

  const openModal = () => {
    document.querySelector("#formModal").style.display = "flex";
    setModalKey(prevKey => prevKey + 1);
  }

  const closeModal = (e) => {
    if (e.target.id === "formContainer") {
      document.querySelector("#formModal").style.display = "none";
      setFormToggle(false);
    }
  }

  const toggleUserMenu = () => {
    if(document.querySelector("#userMenu").style.display === "flex") {

      document.querySelector("#userMenu").style.display = "none";  

    } else {
      document.querySelector("#userMenu").style.display = "flex";  

    }
  }

  return (
    <>
      <nav>
        <div id='menuIcon' className='imgAlign'>
          <a href="/" className='nav-links titlesFont' id='logo'>VideoApp</a>
        </div>
        <div className='paddingAlign imgAlign'>
          <input placeholder='Search' type="text" name="videoTitle" id="" />
          <button id='searchBtn'>
            <SearchIcon />
          </button>
        </div>
        {user.username ? (
          <div className='paddingAlign' id='userInfo'>
            <UserMenu username={user.username} />
            <img id='avatar' src={avatar} alt="Profile Avatar"  onClick={toggleUserMenu}/>
          </div>
        ) : (
          <div className='paddingAlign' id='userInfo'>
            <a className='nav-links' onClick={openModal}>Sign in</a>
          </div>
        )}
      </nav>

      <div id='formModal'>
        <div id='formContainer' onClick={closeModal}>
          {formToggle ? 
            <Register key={modalKey} formToggle={formToggle} setFormToggle={setFormToggle} /> : 
            <Login setUser={setUser} key={modalKey} formToggle={formToggle} setFormToggle={setFormToggle} />}
        </div>
      </div>
    </>
  );
}
