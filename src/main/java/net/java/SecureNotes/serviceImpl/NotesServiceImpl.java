package net.java.SecureNotes.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.java.SecureNotes.Repository.NoteRepository;
import net.java.SecureNotes.entity.Note;
import net.java.SecureNotes.service.NotesService;
import net.java.SecureNotes.service.auditlogService;

@Service
@AllArgsConstructor
public class NotesServiceImpl implements NotesService {
	
	private NoteRepository notesRepo;
	@Autowired
	auditlogService auditservice;

	@Override
	public Note createNotes(String username, String content) {
		Note note =new Note();
		note.setContent(content);
		note.setOwnerUsername(username);
		auditservice.logNoteCreation(username, note.getId());
		return notesRepo.save(note);
	}

	@Override
	public Note updateNotes(Long id, String username, String content) {
		Note note=notesRepo.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));
		note.setContent(content);
		auditservice.logNoteUpdate(username, id);
		return notesRepo.save(note);
	}

	@Override
	public void deleteNotes(Long id, String username) {
		Note note=notesRepo.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));
		  if (!note.getOwnerUsername().equals(username)) {
		        throw new RuntimeException("You are not authorized to delete this note");
		    }
		  auditservice.logNoteDelete(username, id);
		notesRepo.delete(note);
	}

	@Override
	public List<Note> getNotesForUser(String userName) {
		List<Note> notes=notesRepo.findByOwnerUsername(userName);
		return notes;
	}
	

}
