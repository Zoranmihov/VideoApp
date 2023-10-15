// src/context/AppProvider.js
import React, { useState } from 'react';
import AppContext from './AppContext';

const AppProvider = ({ children }) => {
  let [user, setUser] = useState({});
  let [videoDetails, setVideoDetails] = useState({});


  const value = {
    user,
    setUser,
    videoDetails,
    setVideoDetails,
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );
};

export default AppProvider;
