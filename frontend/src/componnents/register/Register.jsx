import React from 'react'
import "./register.css"

import { checkUsername, checkPassword, checkCPassword, resetForm, openFilePicker, registerUser, updateAvartar } from './registerVarifications';

export default function register({ formToggle, setFormToggle }) {

  return (
    <>
      <form id='registerForm' onReset={() => resetForm()} onSubmit={registerUser}>
        <h1 id='title' className='titlesFont'>Register</h1>
        <p className='error' id='registerError'></p>
        <label className='registerLabel'>Username:</label>
        <input required onChange={(e) => {
          checkUsername(e);
        }} placeholder='Username' className='registerInput' type="text" name='username' />
        <label className='registerLabel'>Password:</label>
        <input required onChange={(e) => {
          checkPassword(e);
        }} placeholder='Enter your password' className='registerInput' type="password" name='password' />
        <label className='registerLabel'>Confirm password:</label>
        <input required onChange={(e) =>  {
          checkCPassword(e)
        }} placeholder='Confirm your password' className='registerInput' type="password" name='Cpassword' />
        <label className='registerLabel'>Avatar (optional):</label>
        <p id='avatarName'></p>
        <button className='registerBtn fileBtn' type='button' onClick={() => openFilePicker()}>Browse...</button>
        <input id='avatarImg' className='registerFileInput' type="file" name='avatar' onChange={(e) => updateAvartar(e)} />
        <div>
          <button disabled className='registerBtn' id='registerBtn' type='submit'>Register</button>
          <button className='registerBtn' type='reset'>Clear</button>
        </div>
        <p className='registerInfo'>Already have an account? Click <span id='toggleForm' onClick={() => setFormToggle(!formToggle)}>here</span> to register</p>
      </form>
      <div id='avatarPreview'>
        <img id="avatarImgDisplay" src="" alt="" />
      </div>
    </>
  )
}
