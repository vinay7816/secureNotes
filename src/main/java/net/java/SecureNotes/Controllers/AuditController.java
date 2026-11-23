package net.java.SecureNotes.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.java.SecureNotes.entity.AuditLogs;
import net.java.SecureNotes.service.auditlogService;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

	@Autowired
	auditlogService auditservice;
	
	@GetMapping("/notes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AuditLogs> getAllAudits(){
		return auditservice.getAllUsers();
	}
	
	@GetMapping("/note/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AuditLogs> getNoteAuditLogs(@PathVariable Long noteid){
		return auditservice.getUserById(noteid);
	}
}
 