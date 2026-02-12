import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import ReadinFrontend from './ReadinFrontend.jsx';
import './index.css';

createRoot(document.body).render(
  <StrictMode>
    <BrowserRouter>
      <ReadinFrontend />
    </BrowserRouter>
  </StrictMode>
);
