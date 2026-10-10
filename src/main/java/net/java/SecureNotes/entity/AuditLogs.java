package net.java.SecureNotes.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
public class AuditLogs {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
private String action;
private String username;
private Long noteId;
private String noteContent;
private LocalDateTime timestamp;

}
 