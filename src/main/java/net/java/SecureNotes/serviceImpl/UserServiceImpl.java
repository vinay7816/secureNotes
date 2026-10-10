package net.java.SecureNotes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.java.SecureNotes.Repository.RoleRepository;
import net.java.SecureNotes.Repository.UserRepository;
import net.java.SecureNotes.dtoClasses.UserDto;
import net.java.SecureNotes.entity.AppRole;
import net.java.SecureNotes.entity.Role;
import net.java.SecureNotes.entity.User;
import net.java.SecureNotes.mappers.UserMapper;
import net.java.SecureNotes.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userrepo;
	private final RoleRepository rolerepo;

	@Override
	public void updateUserRole(Long id, String roleName) {
		User user=userrepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
		AppRole approle=AppRole.valueOf(roleName);
		Role role=rolerepo.findByRoleName(approle).orElseThrow(()->new RuntimeException("User not found"));
		user.setRole(role);
		userrepo.save(user);
		
		
		
	}

	@Override
	public List<User> getAllUsers() {
		List<User> allusers=userrepo.findAll();
		return allusers;
	}

	@Override
	public UserDto getuserById(Long id) {
		User user=userrepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
		UserDto dto=UserMapper.convertToDto(user);
		return dto;
	}
	 @Override
	    public User findByUsername(String username) {
	        Optional<User> user = userrepo.findByUserName(username);
	        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
	    }
	

}
