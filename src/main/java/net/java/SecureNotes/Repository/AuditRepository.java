package net.java.SecureNotes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.java.SecureNotes.entity.AuditLogs;

public interface AuditRepository extends JpaRepository<AuditLogs,Long> {

	List<AuditLogs> findByNoteId(Long noteId);  
}
