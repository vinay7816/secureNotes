package net.java.SecureNotes.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import net.java.SecureNotes.dtoClasses.UserDto;
import net.java.SecureNotes.entity.User;

public interface UserService {
 
	void updateUserRole(Long id,String roleName);
	public List<User> getAllUsers();
	UserDto getuserById(Long id);
	public User findByUsername(String username);
	
}
