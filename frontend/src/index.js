import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { ContextProvider as MyContextProvider } from './components/store/ContextApi';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <MyContextProvider>
      <App />
   
  </MyContextProvider>
);
    
reportWebVitals();
