import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import ReadinFrontend from './ReadinFrontend.jsx'
import './index.css'

createRoot(document.body).render(<StrictMode>
    <ReadinFrontend/>
</StrictMode>,)
