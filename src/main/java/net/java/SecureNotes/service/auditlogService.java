package net.java.SecureNotes.service;

import java.util.List;

import lombok.Data;
import net.java.SecureNotes.entity.AuditLogs;


public interface auditlogService {

	void logNoteCreation(String username,Long noteId);
	void logNoteUpdate(String username,Long noteId);
	void logNoteDelete(String username,Long noteId);
	List<AuditLogs> getAllUsers();
	List<AuditLogs> getUserById(Long noteid);
	
}
