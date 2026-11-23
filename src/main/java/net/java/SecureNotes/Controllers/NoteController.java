package net.java.SecureNotes.Controllers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.java.SecureNotes.entity.Note;
import net.java.SecureNotes.serviceImpl.NotesServiceImpl;

@RestController
@RequestMapping("/api/securenotes")
@AllArgsConstructor
public class NoteController {

	private NotesServiceImpl notesService;
	
	@PostMapping("/add")
	public Note createNote(@RequestBody String content,
			@AuthenticationPrincipal UserDetails userdetails) {
		String username=userdetails.getUsername();
		return notesService.createNotes(username, content);
	}
	  @GetMapping("/get")
	    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
	        String username = userDetails.getUsername();
	        System.out.println("USER DETAILS: " + username);
	        return notesService.getNotesForUser(username);
	    }

	    @PutMapping("/{noteId}")
	    public Note updateNote(@PathVariable Long noteId,
	                           @RequestBody String content,
	                           @AuthenticationPrincipal UserDetails userDetails) {
	        String username = userDetails.getUsername();
	        return notesService.updateNotes(noteId, username, content);
	    }

	    @DeleteMapping("/{noteId}")
	    public void deleteNote(@PathVariable Long noteId,
	                           @AuthenticationPrincipal UserDetails userDetails) {
	        String username = userDetails.getUsername();
	        notesService.deleteNotes(noteId, username);
	    }
}
