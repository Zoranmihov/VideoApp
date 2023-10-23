import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./nav.css";

// Components
import Login from '../login/Login';
import Register from '../register/Register';
import UserMenu from '../UserMenu/UserMenu';
import SearchIcon from '../SearchIcon/SearchIcon';
import Avatar from '../Avatar/Avatar';

export default function Nav({ user, setUser }) {

  const [formToggle, setFormToggle] = useState(false);
  const [modalKey, setModalKey] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const setAvatarSrc = (base64String) => {
      document.getElementById('avatarNav').src = `data:image/jpeg;base64,${base64String}`;
    };

    if (user.avatar) {
      setAvatarSrc(user.avatar);
    }
  }, [user.avatar]);




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
    if (document.querySelector("#userMenu").style.display === "flex") {

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
        <form onSubmit={(e) => {
          e.preventDefault();
          navigate("/search/" + e.target.videoTitle.value);
        }}>
          <div className='paddingAlign imgAlign'>
            <input placeholder='Search' type="text" name="videoTitle" id="" />
            <button type='submit' id='searchBtn'>
              <SearchIcon />
            </button>
          </div>
        </form>
        {user.username ? (
          <div className='paddingAlign' id='userInfo'>
            <UserMenu username={user.username} />
            <div id='avatar' onClick={toggleUserMenu}>
              {user.avatar ? (
                <img
                  src=""
                  alt="User Avatar"
                  loading="lazy"
                  id='avatarNav'
                />
              ) : (
                <Avatar width={"35px"} height={"35px"} />
              )}
            </div>
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
