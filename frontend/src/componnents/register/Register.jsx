import React from 'react'
import "./register.css"

import { checkUsername, checkPassword, checkCPassword, resetForm, axiosRegister } from './registerVarifications';

export default function register({ formToggle, setFormToggle }) {

  let registerUser = async (event) => {
    event.preventDefault();
    const username = event.target.username.value;
    const password = event.target.password.value;

    if(password != event.target.Cpassword.value) {
      document.querySelector("#title").style.color = 'red';
      let registerError = document.querySelector("#registerError")
      registerError.innerHTML = "Passwords don't match";
      registerError.style.display = "inline"
      return

    }

    document.querySelector("#title").style.color = 'black';
    document.querySelector("#registerError").style.display = 'none';

    axiosRegister(username, password);
  }

  return (
    <form id='registerForm' onReset={() => resetForm()} onSubmit={registerUser}>
      <h1 id='title' className='titlesFont'>Register</h1>
      <p className='error' id='registerError'></p>
      <label className='registerLabel'>Username:</label>
      <input required onChange={(e) => checkUsername(e.target)} placeholder='Username' className='registerInput' type="text" name='username' />
      <label className='registerLabel'>Password:</label>
      <input required onChange={(e) => checkPassword(e.target)} placeholder='Enter your password' className='registerInput' type="password" name='password' />
      <label className='registerLabel'>Confirm password:</label>
      <input required onChange={(e) => checkCPassword(e.target)} placeholder='Confirm your password' className='registerInput' type="password" name='Cpassword' />
      <div>
        <button disabled className='registerBtn' id='registerBtn' type='submit'>Register</button>
        <button className='registerBtn' type='reset'>Clear</button>
      </div>
      <p className='registerInfo'>Already have an account? Click <span id='toggleForm' onClick={() => setFormToggle(!formToggle)}>here</span> to register</p>
    </form>
  )
}
