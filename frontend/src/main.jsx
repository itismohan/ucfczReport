import React from 'react'
import { createRoot } from 'react-dom/client'
import UCFRDashboard from './UCFRDashboard'

function App(){
  return <UCFRDashboard />
}

createRoot(document.getElementById('root')).render(<App />)
