import React, { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useMyContext } from '../store/ContextApi';

const getRoleLabel = (user) => {
  const roles = Array.isArray(user?.roles)
    ? user.roles
    : typeof user?.roles === 'string'
      ? user.roles.split(',')
      : [];
  const role = roles[0]?.replace(/^ROLE_/i, '').replace(/[_-]/g, ' ');

  if (!role) {
    return 'User';
  }

  return role.toLowerCase().replace(/\b\w/g, (character) => character.toUpperCase());
};

const Navbar = () => {
  const { token, user, setToken, setUser } = useMyContext();
  const navigate = useNavigate();
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const isLoggedIn = Boolean(token);
  const roleLabel = getRoleLabel(user);

  const handleLogout = () => {
    localStorage.removeItem('JWT_TOKEN');
    localStorage.removeItem('USER');
    localStorage.removeItem('IS_ADMIN');
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
    setIsProfileOpen(false);
    navigate('/', { replace: true });
  };

  const linkClassName = ({ isActive }) =>
    `navbar-link${isActive ? ' navbar-link-active' : ''}`;

  return (
    <header className="navbar">
      <div className="navbar-inner">
        <Link className="navbar-brand" to="/" aria-label="SecureNotes home">
          <span className="navbar-brand-mark" aria-hidden="true">S</span>
          <span>Secure<span>Notes</span></span>
        </Link>

        <nav className="navbar-links" aria-label="Main navigation">
          <NavLink className={linkClassName} to="/" end>Home</NavLink>
          <NavLink className={linkClassName} to="/about">About</NavLink>
          <NavLink className={linkClassName} to="/contact">Contact</NavLink>
          {isLoggedIn && (
            <>
              <NavLink className={linkClassName} to="/notes">My notes</NavLink>
              <NavLink className={linkClassName} to="/dashboard">Dashboard</NavLink>
            </>
          )}
        </nav>

        <div className="navbar-actions">
          {isLoggedIn ? (
            <>
              <div className="navbar-profile-wrap">
                <button
                  className="navbar-profile"
                  type="button"
                  aria-expanded={isProfileOpen}
                  aria-haspopup="true"
                  aria-label={`Profile: signed in with the ${roleLabel} role`}
                  onClick={() => setIsProfileOpen((isOpen) => !isOpen)}
                >
                  <span className="navbar-avatar" aria-hidden="true">
                    {roleLabel.charAt(0)}
                  </span>
                  <span className="navbar-profile-copy">
                    <span className="navbar-profile-caption">Profile</span>
                    <strong>{roleLabel}</strong>
                  </span>
                </button>
                {isProfileOpen && (
                  <div className="navbar-profile-menu" role="group" aria-label="Profile details">
                    <span className="navbar-profile-menu-label">Signed in as</span>
                    <strong>{user?.username || 'Account'}</strong>
                    <span className="navbar-profile-role">{roleLabel} role</span>
                  </div>
                )}
              </div>
              <button className="navbar-logout" type="button" onClick={handleLogout}>
                Log out
              </button>
            </>
          ) : (
            <>
              <Link className="navbar-login" to="/login">Log in</Link>
              <Link className="navbar-signup" to="/signup">Get started</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Navbar;
