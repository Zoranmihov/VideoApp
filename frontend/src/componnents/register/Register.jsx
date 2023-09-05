import React from 'react'
import "./register.css"

import { checkUsername, checkPassword, resetForm } from './registerVarifications';

export default function register({ formToggle, setFormToggle }) {

  let registerUser = async (event) => {
    event.preventDefault(); // Prevent the default form submission behavior

  const username = event.target.username.value;
  const password = event.target.password.value;

  try {
    const response = await fetch('https://localhost:8443/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    console.log(data);

  } catch (error) {
    console.error('There was a problem with the fetch operation:', error.message);
  }
}

  return (
    <form id='registerForm' onReset={() => resetForm()} onSubmit={registerUser}>
      <h1 id='title titlesFont' className='titlesFont'>Register</h1>
      <p id='error'>Test</p>
      <label className='registerLabel'>Username:</label>
      <input onChange={(e) => checkUsername(e.target)} placeholder='Username' className='registerInput' type="text"  name='username'/>
      <label  className='registerLabel'>Password:</label>
      <input onChange={(e) => checkPassword(e.target)} placeholder='Enter your password' className='registerInput' type="password" name='password'/>
      <label  className='registerLabel'>Confirm password:</label>
      <input placeholder='Confirm your password' className='registerInput' type="password" name='Cpassword'/>
      <div>
      <button className='registerBtn' id='registerBtn' type='submit'>Register</button>
      <button className='registerBtn' type='reset'>Clear</button>
      </div>
      <p className='registerInfo'>Already have an account? Click <span onClick={() => setFormToggle(!formToggle)}>here</span> to register</p>
    </form>
  )
}
