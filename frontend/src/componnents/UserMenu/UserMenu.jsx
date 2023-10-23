import React, {useState, useEffect} from 'react';
import "./userMenu.css"

export default function UserMenu({username}) {

  const [menuVisible, setMenuVisible] = useState(false);

    const toggleUserMenu = () => {
        setMenuVisible(!menuVisible);
    }

    const closeMenu = (e) => {
        if (e.target.id !== "userMenu") {
            setMenuVisible(false);
        }
    }

    useEffect(() => {
        const handleDocumentClick = (e) => {
            if (e.target.id !== "userMenu" && !e.target.closest("#userMenu")) {
                setMenuVisible(false);
            }
        }

        document.addEventListener('click', handleDocumentClick);

        return () => {
            document.removeEventListener('click', handleDocumentClick);
        }
    }, []);

    return(
        <div id='userMenu'>
            <p className='titlesFont' id='username'>{username}</p>
            
            <a href="/upload">Upload a video</a>
            <a href="/userprofile">Profile</a>

        </div>
    )
}
