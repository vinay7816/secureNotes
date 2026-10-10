package net.java.SecureNotes.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.java.SecureNotes.dtoClasses.UserDto;
import net.java.SecureNotes.entity.User;
import net.java.SecureNotes.service.UserService;
import net.java.SecureNotes.serviceImpl.UserServiceImpl;


@RestController
@RequestMapping("/api/notes")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	
	@Autowired
	private UserServiceImpl userservice;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users=userservice.getAllUsers();
		return new ResponseEntity<>(users,HttpStatus.OK);
	}

	  @PutMapping("/update-role")
	    public ResponseEntity<String> updateUserRole(@RequestParam Long userId, 
	                                                 @RequestParam String roleName) {
	        userservice.updateUserRole(userId, roleName);
	        return ResponseEntity.ok("User role updated");
	    }

	    @GetMapping("/user/{id}")
	    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
	        return new ResponseEntity<>(userservice.getuserById(id),
	                HttpStatus.OK);
	    }
}
