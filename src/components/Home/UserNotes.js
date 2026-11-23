import React, { useState } from 'react';
import { Link, Route, Routes, useNavigate } from 'react-router-dom';
import NotePage from './NotePage';
import api from '../Api/api';
import { useEffect } from 'react';


const UserNotes = () => {
  const [notes, setNotes] = useState([]);
  const navigate = useNavigate();

   useEffect(() => {
    const usernotes=async()=>{    
      try {
        const response = await api.get('/securenotes/get');
        setNotes(response.data);
      } catch (error) {
        console.error('Error fetching user notes:', error);
      }
    };
      usernotes();
  },[]);


  const deleteNote = async(noteid) => {
    try {
      await api.delete(`/securenotes/${noteid}`);
      setNotes((currentNotes) =>
      currentNotes.filter((note) => note.id !== noteid)
    );
      
    }
    catch (error) {
      console.error('Error deleting note:', error);
    }
  };

  const openNote = (note, mode) => {
    navigate('/notes/editor', { state: { note, mode } });
   
  };

  const addNote = () => {
    navigate('/notes/editor', { state: { mode: 'create' } });
  };


 

  return (
          <div className="notes-page">
            <div className="notes-header">
              <div>
                <p className="eyebrow">Workspace</p>
                <h2>User Notes</h2>
              </div>
              <button className="add-note-btn" onClick={addNote}>
                <span className="plus-icon">+</span>
                Add Note
              </button>
            </div>

            <div className="notes-summary">
              <div className="summary-card">
                <span className="summary-icon">📝</span>
                <div>
                  <strong>{notes.length}</strong>
                  <p>Total Notes</p>
                </div>
              </div>
              <div className="summary-card">
                <span className="summary-icon">📌</span>
                <div>
                  <strong>8</strong>
                  <p>Important</p>
                </div>
              </div>
              <div className="summary-card">
                <span className="summary-icon">🗂️</span>
                <div>
                  <strong>6</strong>
                  <p>Archived</p>
                </div>
              </div>
            </div>

            <div className="notes-panel">
              <div className="notes-toolbar">
                <h3>Recent notes</h3>
                <span>Here you can view and manage your notes.</span>
              </div>

              <div className="notes-list">
                {notes.map((note) => (
                  <article className="note-card" key={note.id}>
                    <div className="note-topbar">
                      <span className="note-tag">{note.date}</span>
                      <div className="note-actions">
                        <button
                          className="mini-btn view-btn"
                          onClick={() => openNote(note, 'view')}
                        >
                          View
                        </button>
                        <button
                          className="mini-btn edit-btn"
                          onClick={() => openNote(note, 'edit')}
                        >
                          Edit
                        </button>
                        <button
                          className="mini-btn delete-btn"
                          onClick={() => deleteNote(note.id)}
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                    <h4>{note.title}</h4>
                    <p>{note.content}</p>
                  </article>
                ))}
              </div>
            </div>
          </div>
  );
};

export default UserNotes;
