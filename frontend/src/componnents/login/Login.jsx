import React from 'react'
import "./login.css"

export default function Login({ formToggle, setFormToggle }) {

  let loginUser = async (event) => {
    event.preventDefault(); // Prevent the default form submission behavior

    const username = event.target.username.value;
    const password = event.target.password.value;

    try {
      const response = await fetch('https://localhost:8443/auth/login', {
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
