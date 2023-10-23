import React, {useContext} from 'react'
import "./login.css"

import axios from 'axios';
import AppContext from '../../context/AppContext';

export default function Login({ formToggle, setFormToggle, setUser }) {

  let {setJwtToken} = useContext(AppContext);

  let loginUser = async (event) => {
    event.preventDefault();
    document.querySelector("#title").style.color = 'black';
    document.querySelector("#loginError").style.display = 'none';


    const username = event.target.username.value;
    const password = event.target.password.value;


    axios.post('https://localhost:8443/auth/login', { username, password }, { withCredentials: true }).then(res => {
      document.querySelector("#title").innerHTML = "Welcome";
      setJwtToken(res.data.jwt);
      setUser(res.data);
      setTimeout(() => {
        document.querySelector("#formModal").style.display = "none";
        document.querySelector("#loginForm").reset();
      }, 1100)
    }).catch(err => {
      document.querySelector("#title").style.color = 'red';
      let loginError = document.querySelector("#loginError")
      loginError.innerHTML = "Invalid credentials";
      loginError.style.display = "inline"
      document.querySelector("#loginForm").reset();
    })
  }

  return (
    <form id='loginForm' onSubmit={loginUser}>
      <h1 id='title' className='titlesFont'>Login</h1>
      <p className='error' id='loginError'>Test</p>
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
