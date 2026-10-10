package net.java.SecureNotes.service;

import java.util.List;

import lombok.Data;
import net.java.SecureNotes.entity.Note;


public interface NotesService {
  Note createNotes(String username,String content);
  Note updateNotes(Long id,String username,String content);
  void deleteNotes(Long id,String username);
  List<Note> getNotesForUser(String userName);
}
