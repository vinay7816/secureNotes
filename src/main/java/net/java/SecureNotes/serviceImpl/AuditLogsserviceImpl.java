package net.java.SecureNotes.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.java.SecureNotes.Repository.AuditRepository;
import net.java.SecureNotes.entity.AuditLogs;
import net.java.SecureNotes.service.auditlogService;



@Service
public class AuditLogsserviceImpl implements auditlogService {
	@Autowired
	AuditRepository auditrepo;

	@Override
	public void logNoteCreation(String username, Long noteId) {
		AuditLogs auditLog=new AuditLogs();
		auditLog.setAction("Create");
		auditLog.setNoteId(noteId);
		auditLog.setTimestamp(LocalDateTime.now());
		auditLog.setUsername(username);
		auditrepo.save(auditLog);
		
	}

	@Override
	public void logNoteUpdate(String username, Long noteId) {
		AuditLogs auditLog=new AuditLogs();
		auditLog.setAction("Update");
		auditLog.setNoteId(noteId);
		auditLog.setTimestamp(LocalDateTime.now());
		auditLog.setUsername(username);	
		auditrepo.save(auditLog);
	}

	@Override
	public void logNoteDelete(String username, Long noteId) {
		AuditLogs auditLog=new AuditLogs();
		auditLog.setAction("Delete");
		auditLog.setNoteId(noteId);
		auditLog.setTimestamp(LocalDateTime.now());
		auditLog.setUsername(username);
		auditrepo.save(auditLog);
		
	}

	@Override
	public List<AuditLogs> getAllUsers() {
	   List<AuditLogs> allAudits=auditrepo.findAll();
		return allAudits;
	}

	@Override
	public List<AuditLogs> getUserById(Long noteid) {
		
		return auditrepo.findByNoteId(noteid);
	}

}
