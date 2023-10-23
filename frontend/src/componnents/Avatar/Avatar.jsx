import React, {useEffect} from 'react'
import './avatar.css'

export default function Avatar({ width, height }) {

    return (
        <>
            <img className='noAvatar' style={{width: width, height: height}} src="./src/assets/noAvatar.png" alt="no avatar" />
        </>
    )
}
