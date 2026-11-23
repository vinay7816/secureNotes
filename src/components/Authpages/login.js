import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../Api/api';
import { useMyContext } from '../store/ContextApi';

const Login = () => {
  const [data, setData] = useState({ username: '', password: '', code: '' });
  const [step, setStep] = useState(1);
  const [pendingToken, setPendingToken] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');
  const { setToken, setUser } = useMyContext();
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setData((previous) => ({ ...previous, [name]: value }));
  };

  const completeLogin = (token, decodedToken) => {
    const roles = Array.isArray(decodedToken.roles)
      ? decodedToken.roles
      : decodedToken.roles
        ? decodedToken.roles.split(',')
        : [];
    const user = { username: decodedToken.sub, roles };

    localStorage.setItem('JWT_TOKEN', token);
    localStorage.setItem('USER', JSON.stringify(user));
    setToken(token);
    setUser(user);
    navigate('/notes');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError('');

    try {
      const response = await api.post('/auth/public/login', {
        username: data.username,
        password: data.password,
      });

      if (response.status !== 200 || !response.data.jwtToken) {
        setError('Login failed: no authentication token was received.');
        return;
      }

      const token = response.data.jwtToken;
      const decodedToken = jwtDecode(token);

      if (decodedToken.is2faEnabled) {
        setPendingToken(token);
        setStep(2);
      } else {
        completeLogin(token, decodedToken);
      }
    } catch (requestError) {
      console.error('Login failed:', requestError);
      setError(
        requestError.response?.data?.message || (
          requestError.response
            ? 'Invalid username or password.'
            : 'Unable to connect to the server. Please try again.'
        )
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const verifyTwoFactor = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError('');

    try {
      const formData = new URLSearchParams();
      formData.append('code', data.code);
      formData.append('jwtToken', pendingToken);

      await api.post('/auth/public/verify-2fa-login', formData, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      });

      completeLogin(pendingToken, jwtDecode(pendingToken));
    } catch (requestError) {
      console.error('2FA verification failed:', requestError);
      setError(
        requestError.response?.data?.message ||
          'The verification code was not accepted. Please try again.'
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
          <h1>
            {step === 1
              ? 'Keep your ideas safe, organized, and private.'
              : 'One more step to your workspace.'}
          </h1>
          <p>
            {step === 1
              ? 'Access your personal notes from anywhere and never lose a brilliant idea again.'
              : 'Verify your identity with the code from your authenticator app.'}
          </p>
          <ul className="feature-list">
            <li>Private note storage</li>
            <li>A focused, organized workspace</li>
            <li>Extra sign-in protection</li>
          </ul>
        </section>

        <section className="auth-panel">
          <div className="auth-header">
            <p className="eyebrow">{step === 1 ? 'Welcome back' : 'Extra security'}</p>
            <h2>{step === 1 ? 'Sign in to your account' : 'Verify your sign in'}</h2>
          </div>

          {step === 1 ? (
            <form className="auth-form" onSubmit={handleSubmit}>
              <label htmlFor="username">Username</label>
              <input
                autoComplete="username"
                id="username"
                name="username"
                type="text"
                value={data.username}
                onChange={handleChange}
                placeholder="Enter your username"
                required
              />

              <label htmlFor="password">Password</label>
              <input
                autoComplete="current-password"
                id="password"
                name="password"
                type="password"
                value={data.password}
                onChange={handleChange}
                placeholder="Enter your password"
                required
              />

              <button type="submit" className="primary-btn" disabled={isSubmitting}>
                {isSubmitting ? 'Signing in…' : 'Sign in'}
              </button>
            </form>
          ) : (
            <form className="auth-form" onSubmit={verifyTwoFactor}>
              <label htmlFor="code">Authentication code</label>
              <input
                autoComplete="one-time-code"
                id="code"
                inputMode="numeric"
                name="code"
                onChange={handleChange}
                placeholder="Enter your 2FA code"
                required
                type="text"
                value={data.code}
              />
              <button type="submit" className="primary-btn" disabled={isSubmitting}>
                {isSubmitting ? 'Verifying…' : 'Verify and continue'}
              </button>
            </form>
          )}

          {error && <p className="auth-error" role="alert">{error}</p>}
          <p className="switch-copy">
            {step === 1 ? (
              <>Don’t have an account? <Link to="/signup">Create one</Link></>
            ) : (
              'Your account is protected with two-step verification.'
            )}
          </p>
        </section>
      </div>
    </main>
  );
};

export default Login;
