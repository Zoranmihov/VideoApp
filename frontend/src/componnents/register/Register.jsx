import React from 'react'
import "./register.css"

import { checkUsername, checkPassword, resetForm } from './registerVarifications';

export default function register({ formToggle, setFormToggle }) {

  let registerUser = async (event) => {
    event.preventDefault(); // Prevent the default form submission behavior

    const username = event.target.username.value;
    const password = event.target.password.value;

    axios.post('https://localhost:8443/auth/login', { username, password }).then(res => {
      document.querySelector("#title").innerHTML = res.body
    }).catch(err => {
      console.log(err)
    })
  }

  return (
    <form id='registerForm' onReset={() => resetForm()} onSubmit={registerUser}>
      <h1 id='title titlesFont' className='titlesFont'>Register</h1>
      <p id='error'>Test</p>
      <label className='registerLabel'>Username:</label>
      <input onChange={(e) => checkUsername(e.target)} placeholder='Username' className='registerInput' type="text" name='username' />
      <label className='registerLabel'>Password:</label>
      <input onChange={(e) => checkPassword(e.target)} placeholder='Enter your password' className='registerInput' type="password" name='password' />
      <label className='registerLabel'>Confirm password:</label>
      <input placeholder='Confirm your password' className='registerInput' type="password" name='Cpassword' />
      <div>
        <button className='registerBtn' id='registerBtn' type='submit'>Register</button>
        <button className='registerBtn' type='reset'>Clear</button>
      </div>
      <p className='registerInfo'>Already have an account? Click <span onClick={() => setFormToggle(!formToggle)}>here</span> to register</p>
    </form>
  )
}
