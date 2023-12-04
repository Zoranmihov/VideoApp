import { BrowserRouter, Routes, Route, json } from 'react-router-dom';
import React, { useContext, useEffect, useState } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import axios from 'axios';

// Elements
import Nav from './componnents/nav/Nav.jsx';
import Index from './pages/index/Index.jsx';
import Loading from './pages/Loading/Loading';
import UploadVideo from './pages/UploadVideo/UploadVideo';
import VideoPlayer from './pages/videoPlayer/VideoPlayer';
import SearchPage from './pages/SearchPage/SearchPage';
import UserProfile from './pages/UserProfile/UserProfile.jsx';
import UserPage from './pages/UserPage/UserPage.jsx';

// Guards
import ProtectedRoutes from './componnents/protectedRoutes/ProtectedRoutes';

// Context
import AppProvider from './context/AppProvider';
import AppContext from './context/AppContext';

function App() {
  let { user, setUser } = useContext(AppContext);
  let [loading, setLoading] = useState(true);
  const [searchKey, setSearchKey] = useState(0);

  const resetSearch = () => {
    setSearchKey(prevKey => prevKey + 1);
};


   useEffect(() => {
  /* Set automated login */
   axios.post('https://localhost:8443/auth/login', { username: "superAdmin", password: "password" }, { withCredentials: true }).then(res => {
     setUser(res.data)
     setLoading(false)
   }).catch(err => {
     console.log(err)
     setLoading(false)
   })


  axios.post('https://localhost:8443/auth/userinfo', {}, {
    withCredentials: true,
  }).then(res => {
    setUser(res.data)
    setLoading(false)
  }).catch(err => {
    console.log(err)
    setUser({})
    setLoading(false)
  })

    }, []);

  return (
    <>
      {loading ? (<Loading />) : (
        <>
          <BrowserRouter>
          <Nav user={user} setUser={setUser} />
            <Routes>
              <Route index element={<Index />} />
              <Route path='/videoplayer/:videoId' element={<VideoPlayer />} />
              <Route path='/user/:username' element={<UserPage />} />
              <Route path='/search/:search' element={<SearchPage key={searchKey} resetSearch={resetSearch} />} />
              {/* Protected routes   */}
              <Route element={<ProtectedRoutes username={user.username} />}>
                <Route path='/upload' element={<UploadVideo />} />
                <Route path='/userprofile' element={<UserProfile />} />
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
    <AppProvider>
      <App />
    </AppProvider>

  </React.StrictMode>
);
