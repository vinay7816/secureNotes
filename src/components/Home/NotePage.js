import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../Api/api';

const NotePage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { note, mode = 'create' } = location.state || {};

  const [title, setTitle] = useState(note?.title || '');
  const [content, setContent] = useState(note?.content || '');
  const [isSaving, setIsSaving] = useState(false);

  const isViewMode = mode === 'view';
  const isEditing = mode === 'edit';

  const handleSubmit = async (event) => {
    event.preventDefault();

    const trimmedTitle = title.trim();
    const trimmedContent = content.trim();

    if (!trimmedTitle || isSaving) return;

    setIsSaving(true);

    try {
      if (isEditing) {
        // Update the existing note.
        // Replace this endpoint with your actual backend update endpoint.
        await api.put(`/securenotes/${note.id}`, {
          title: trimmedTitle,
          content: trimmedContent,
        });
      } else {
        // Create a new note.
        await api.post('/securenotes/add', {
          title: trimmedTitle,
          content: trimmedContent,
        });
      }

      navigate('/notes');
    } catch (error) {
      console.error('Error saving note:', error);
    } finally {
      setIsSaving(false);
    }
  };

  const handleEdit = () => {
    navigate('/notes/editor', {
      state: { note, mode: 'edit' },
    });
  };

  const pageTitle = isViewMode
    ? 'Note details'
    : isEditing
    ? 'Edit note'
    : 'Create a note';

  return (
    <main className="note-editor-page">
      <div className="note-editor-shell">
        <header className="note-editor-header">
          <div>
            <p className="eyebrow">Personal workspace</p>
            <h1>{pageTitle}</h1>
            <p className="note-editor-subtitle">
              {isViewMode
                ? 'Review your note and edit it whenever you need.'
                : 'Capture your thoughts in a clear, organized space.'}
            </p>
          </div>

          <button
            className="note-back-btn"
            type="button"
            onClick={() => navigate('/notes')}
          >
            Back to notes
          </button>
        </header>

        <form
          className="note-editor-card"
          onSubmit={handleSubmit}
        >
          <div className="note-editor-meta">
            <span className="note-editor-status">
              <span className="status-dot" />
              {isViewMode ? 'Read only' : 'Private note'}
            </span>

            <span className="note-editor-hint">
              {isViewMode ? note?.date : 'Your changes are saved when submitted.'}
            </span>
          </div>

          <label className="note-field-label" htmlFor="note-title">
            Title
          </label>

          <input
            id="note-title"
            className="note-title-input"
            type="text"
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            placeholder="Give your note a title"
            maxLength={120}
            readOnly={isViewMode}
            required
          />

          <label
            className="note-field-label note-content-label"
            htmlFor="note-content"
          >
            Note
          </label>

          <textarea
            id="note-content"
            className="note-content-input"
            value={content}
            onChange={(event) => setContent(event.target.value)}
            placeholder="Start writing your thoughts here..."
            readOnly={isViewMode}
          />

          <footer className="note-editor-footer">
            <span className="note-character-count">
              {content.length} characters
            </span>

            <div className="note-editor-actions">
              {isViewMode ? (
                <button
                  className="note-save-btn"
                  type="button"
                  onClick={handleEdit}
                >
                  Edit note
                </button>
              ) : (
                <>
                  <button
                    className="note-cancel-btn"
                    type="button"
                    onClick={() => navigate('/notes')}
                  >
                    Cancel
                  </button>

                  <button
                    className="note-save-btn"
                    type="submit"
                    disabled={isSaving}
                  >
                    {isSaving
                      ? 'Saving...'
                      : isEditing
                      ? 'Save changes'
                      : 'Save note'}
                  </button>
                </>
              )}
            </div>
          </footer>
        </form>
      </div>
    </main>
  );
};

export default NotePage;