import React, { createContext, useContext, useState } from 'react';

const ContextApi = createContext(null);

const getStoredUser = () => {
  try {
    const storedUser = localStorage.getItem('USER');
    return storedUser ? JSON.parse(storedUser) : null;
  } catch (error) {
    console.error('Unable to read the saved user profile:', error);
    return null;
  }
};

export const ContextProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('JWT_TOKEN'));
  const [user, setUser] = useState(getStoredUser);

  return (
    <ContextApi.Provider value={{ token, setToken, user, setUser }}>
      {children}
    </ContextApi.Provider>
  );
};

export const useMyContext = () => {
  const context = useContext(ContextApi);

  if (!context) {
    throw new Error('useMyContext must be used within a ContextProvider.');
  }

  return context;
};
