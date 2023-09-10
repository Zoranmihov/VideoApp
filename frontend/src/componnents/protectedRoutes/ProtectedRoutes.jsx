import React, { useEffect } from "react"
import { Navigate, Outlet } from "react-router-dom"


export default function ProtectedRoutes({ username }) {
    return username ? (<Outlet />) : (<Navigate to="/" replace={true} />)


}