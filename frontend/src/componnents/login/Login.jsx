import React from 'react'
import "./login.css"

import axios from 'axios';


export default function Login({ formToggle, setFormToggle }) {

  let loginUser = async (event) => {
    event.preventDefault(); // Prevent the default form submission behavior

    

    const username = event.target.username.value;
    const password = event.target.password.value;


    axios.post('https://localhost:8443/auth/login', {username, password}, { withCredentials: true }).then(res => {
      console.log("Worked")
    }).catch(err => {
      console.log(err)
    })
  }

  return (
    <form id='loginForm' onSubmit={loginUser}>
      <h1 id='title' className='titlesFont'>Login</h1>
      <p id='error'>Test</p>
      <label className='loginLabel'>Username:</label>
      <input placeholder='Username' className='loginInput' type="text" name='username' />
      <label className='loginLabel'>Password:</label>
      <input placeholder='Enter your password' className='loginInput' type="password" name='password' />
      <div>
        <button className='loginBtn' type='submit'>Login</button>
        <button className='loginBtn' type='reset'>Clear</button>
      </div>
      <p className='loginInfo'>Don't have an account yet? Click <span onClick={() => setFormToggle(!formToggle)}>here</span> to register</p>
    </form>
  )
}
