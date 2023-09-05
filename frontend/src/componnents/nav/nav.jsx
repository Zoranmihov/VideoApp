import React, { useState } from 'react';
import "./nav.css";

import Login from '../login/Login';
import Register from '../register/Register';

export default function nav() {

  const [loggedIn, setLoggedIn] = useState(false);
  const [formToggle, setFormToggle] = useState(false);
  const [modalKey, setModalKey] = useState(0); // State for modal key

  const openModal = () => {
    document.querySelector("#formModal").style.display = "flex";
    setModalKey(prevKey => prevKey + 1); // Increment the key to force remount
  }

  const closeModal = (e) => {
    if (e.target.id === "formContainer") {
      document.querySelector("#formModal").style.display = "none";
      setFormToggle(false);
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
          <button id='searchBtn'>&#128269;</button>
        </div>
        {loggedIn ? (
          <div className='paddingAlign' id='userInfo'>
            <img id='avatar' src="./avatar.jpg" alt="Profile Avatar" />
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
            <Login key={modalKey} formToggle={formToggle} setFormToggle={setFormToggle} />}
        </div>
      </div>
    </>
  );
}
