import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../Api/api';

const Signup = () => {
  const [user, setUser] = useState({
    fullName: '',
    email: '',
    password: '',
    agreeToTerms: false,
  });
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError('');

    try {
      const response = await api.post('/auth/public/signup', {
        username: user.fullName,
        email: user.email,
        password: user.password,
      });
      if (response.status === 200 || response.status === 201) {
        navigate('/login');
      } else {
        setError(response.data?.message || 'Signup failed. Please try again.');
      }
    } catch (requestError) {
      console.error('Error during signup:', requestError);
      setError(
        requestError.response?.data?.message ||
          'Unable to create your account right now. Please try again.'
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="auth-page">
      <div className="auth-card">
        <section className="auth-visual">
          <div className="brand-badge">SecureNotes</div>
          <h1>Create your private workspace.</h1>
          <p>
            Capture ideas, organize your notes, and keep your thoughts protected with
            end-to-end security built for everyday productivity.
          </p>
          <ul className="feature-list">
            <li>Encrypted note storage</li>
            <li>Smart organization tools</li>
            <li>Sync across all devices</li>
          </ul>
        </section>

        <section className="auth-panel">
          <div className="auth-header">
            <p className="eyebrow">Join today</p>
            <h2>Create an account</h2>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <label htmlFor="fullName">Full name</label>
            <input
              id="fullName"
              name="fullName"
              type="text"
              value={user.fullName}
              onChange={handleChange}
              placeholder="Enter your full name"
              required
            />

            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={user.email}
              onChange={handleChange}
              placeholder="Enter your email"
              required
            />

            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="new-password"
              value={user.password}
              onChange={handleChange}
              placeholder="Create a password"
              required
            />


            <div className="form-row">
              <label className="checkbox-label" htmlFor="agreeToTerms">
                <input
                  id="agreeToTerms"
                  name="agreeToTerms"
                  type="checkbox"
                  required
                  checked={user.agreeToTerms}
                  onChange={handleChange}
                />
                <span>I agree to the terms</span>
              </label>
            </div>

            <button type="submit" className="primary-btn" disabled={isSubmitting}>
              {isSubmitting ? 'Creating account…' : 'Create account'}
            </button>
            {error && <p className="auth-error" role="alert">{error}</p>}
          </form>

          <p className="switch-copy">
            Already have an account? <Link to="/login">Log in</Link>
          </p>
        </section>
      </div>
    </main>
  );
};

export default Signup;
