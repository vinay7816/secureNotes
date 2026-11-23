import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <main className="home-page">
      <section className="home-hero">
        <div className="home-hero-copy">
          <span className="home-kicker">Your thoughts, protected</span>
          <h1>A calmer, safer place for everything on your mind.</h1>
          <p>
            Capture your ideas, keep important details organized, and get back to
            what matters with a private notes workspace.
          </p>
          <div className="home-hero-actions">
            <Link className="home-primary-link" to="/signup">Create your free account</Link>
            <Link className="home-secondary-link" to="/login">I already have an account</Link>
          </div>
          <div className="home-trust-note">
            <span aria-hidden="true">✓</span> Private by design, ready when you are
          </div>
        </div>
        <div className="home-note-preview" aria-label="Example of a saved note">
          <div className="home-preview-topline">
            <span className="home-preview-icon" aria-hidden="true">✦</span>
            <span>MY NOTES</span>
            <span className="home-preview-dot" />
          </div>
          <p className="home-preview-label">TODAY · 9:41 AM</p>
          <h2>Small ideas, big plans</h2>
          <p className="home-preview-text">
            Make space to think. Save the details worth remembering and find them
            again whenever inspiration strikes.
          </p>
          <div className="home-preview-tags">
            <span>Personal</span>
            <span>Ideas</span>
          </div>
          <div className="home-preview-footer">
            <span><i /> Saved securely</span>
            <span>•••</span>
          </div>
        </div>
      </section>
      <section className="home-benefits" aria-label="SecureNotes benefits">
        <article>
          <span className="home-benefit-icon" aria-hidden="true">◇</span>
          <h2>Private workspace</h2>
          <p>Your notes stay organized in a workspace built with privacy in mind.</p>
        </article>
        <article>
          <span className="home-benefit-icon" aria-hidden="true">⌁</span>
          <h2>Easy to find</h2>
          <p>Keep important thoughts together and return to them without the clutter.</p>
        </article>
        <article>
          <span className="home-benefit-icon" aria-hidden="true">↗</span>
          <h2>Made for momentum</h2>
          <p>Move from a passing thought to a plan you can actually follow.</p>
        </article>
      </section>
    </main>
  );
}

export default Home;
