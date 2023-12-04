import React from 'react'
import noAvatar from '../../assets/noAvatar.png';
import './avatar.css'

export default function Avatar({ width, height }) {

    return (
        <>
            <img className='noAvatar' style={{width: width, height: height}} src={noAvatar} alt="no avatar" />
        </>
    )
}
