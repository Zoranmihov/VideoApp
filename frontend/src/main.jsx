import { BrowserRouter, Routes, Route } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import axios from 'axios';

// Elements
import Nav from './componnents/nav/Nav.jsx';
import Index from './pages/index/Index.jsx';
import Loading from './pages/Loading/Loading';
import UploadVideo from './pages/UploadVideo/UploadVideo';
import VideoPlayer from './componnents/videoPlayer/VideoPlayer';

// Guards
import ProtectedRoutes from './componnents/protectedRoutes/ProtectedRoutes';


function App() {
  let [loggedIn, setLoggedIn] = useState(false);
  let [user, setUser] = useState({});
  let [loading, setLoading] = useState(true);



  useEffect(() => {
    /* Set automated login */
    axios.post('https://localhost:8443/auth/login', { username: "superAdmin", password: "password" }, { withCredentials: true }).then(res => {
      setUser(res.data)
      setLoading(false)
    }).catch(err => {
      console.log(err)
      setLoading(false)
    })


    // axios.post('https://localhost:8443/auth/userinfo', {}, {
    //   withCredentials: true,
    // }).then(res => {
    //   setUser(res.data)
    //   setLoading(false)
    // }).catch(err => {
    //   console.log(err)
    //   setUser({})
    //   setLoading(false)
    // })

  }, []);

  return (
    <>
      {loading ? (<Loading />) : (
        <>
          <Nav user={user} setUser={setUser} />
          <BrowserRouter>
            <Routes>
              <Route index element={<Index />} />
              <Route path='/videoplayer' element={<VideoPlayer />} />
              {/* Protected routes   */}
              <Route element={<ProtectedRoutes username={user.username} />}>
                <Route path='/upload' element={<UploadVideo />} />
              </Route>
            </Routes>
          </BrowserRouter>
        </>
      )}
    </>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);