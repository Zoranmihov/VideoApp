import { BrowserRouter, Routes, Route } from 'react-router-dom'
import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'

// Elements
import Nav from './componnents/nav/nav.jsx'
import Index from './pages/index/Index.jsx'


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <Nav />
    <BrowserRouter>
    <Routes>
      <Route index element={<Index />} />
    </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
