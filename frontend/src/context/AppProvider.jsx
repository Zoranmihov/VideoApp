// src/context/AppProvider.js
import React, { useState } from 'react';
import AppContext from './AppContext';

const AppProvider = ({ children }) => {
  let [user, setUser] = useState({});
  let [videoDetails, setVideoDetails] = useState({});
  let [jwtToken, setJwtToken] = useState("");


  const value = {
    user,
    setUser,
    videoDetails,
    setVideoDetails,
    jwtToken,
    setJwtToken
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );
};

export default AppProvider;
