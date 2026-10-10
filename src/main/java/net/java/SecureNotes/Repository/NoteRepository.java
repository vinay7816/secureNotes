package net.java.SecureNotes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.java.SecureNotes.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByOwnerUsername(String ownerUsername);
}
