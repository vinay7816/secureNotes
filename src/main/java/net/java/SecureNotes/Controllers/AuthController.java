package net.java.SecureNotes.Controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.java.SecureNotes.Repository.RoleRepository;
import net.java.SecureNotes.Repository.UserRepository;
import net.java.SecureNotes.entity.AppRole;
import net.java.SecureNotes.entity.Role;
import net.java.SecureNotes.entity.User;
import net.java.SecureNotes.securityConfig.jwtutils.JWTUtils;
import net.java.SecureNotes.securityConfig.request.LoginRequest;
import net.java.SecureNotes.securityConfig.request.Signup;
import net.java.SecureNotes.securityConfig.response.LoginResponse;
import net.java.SecureNotes.securityConfig.response.MessageResponse;
import net.java.SecureNotes.securityConfig.response.userInfoResponse;
import net.java.SecureNotes.service.UserService;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTUtils utils;
	
	@Autowired
	private UserRepository userrepo;
	@Autowired 
	private PasswordEncoder encoder;
	@Autowired
	private RoleRepository rolerepo;
	@Autowired
	UserService userservice;
	
	@PostMapping("/public/login")
	public ResponseEntity<?> userLogin(@RequestBody LoginRequest login){
		Authentication authentication;
		try {
			authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
			
		} catch (AuthenticationException e) {
			Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userdetails=(UserDetails) authentication.getPrincipal();
		String jwtToken=utils.generateJwt(userdetails);
		List<String> roles=userdetails.getAuthorities().stream()
                           .map(item -> item.getAuthority())
                           .collect(Collectors.toList());
		LoginResponse response = new LoginResponse(jwtToken,userdetails.getUsername(),
                roles);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/public/signup")
	public ResponseEntity<?> userSignup(@Valid @RequestBody Signup signup){
		if(userrepo.existsByUserName(signup.getUsername())) {
			return new ResponseEntity<>(
				    new MessageResponse("Error: Username is already taken!"),
				    HttpStatus.BAD_REQUEST
				);		
			}
		if(userrepo.existsByEmail(signup.getEmail())) {
			return new ResponseEntity<>(
				    new MessageResponse("Error: Email id is already taken!"),
				    HttpStatus.BAD_REQUEST
				);
		}
		User user=new User(signup.getUsername(),signup.getEmail(),encoder.encode(signup.getPassword()));
		Set<String> roles=signup.getRole();
		Role role;
		if(roles==null || roles.isEmpty()) {
			role=rolerepo.findByRoleName(AppRole.ROLE_USER).orElseThrow(()-> new RuntimeException("Error: Role is not found."));
		}
		else {
		String strrole = roles.iterator().next();
		if(strrole.equals("admin")){
			role=rolerepo.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()-> new RuntimeException("Error: Role is not found."));
		}
		else if(strrole.equals("user")){
			role=rolerepo.findByRoleName(AppRole.ROLE_USER).orElseThrow(()-> new RuntimeException("Error: Role is not found."));
		}
		else {
	        throw new RuntimeException("Error: Invalid role: " + strrole);
	    }
		
		
	}
		user.setAccountNonLocked(true);
	      user.setAccountNonExpired(true);
	      user.setCredentialsNonExpired(true);
	      user.setEnabled(true);
	      user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
	      user.setAccountExpiryDate(LocalDate.now().plusYears(1));
	      user.setTwoFactorEnabled(false);
	      user.setSignUpMethod("email");
	      user.setRole(role);
	      userrepo.save(user);
	      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
}
	@GetMapping("/user")
	public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userdetails){
		User user=userservice.findByUsername(userdetails.getUsername());
		List<String> strRoles=userdetails.getAuthorities().stream()
				             .map(item -> item.getAuthority())
				             .collect(Collectors.toList());
		 userInfoResponse response = new userInfoResponse(
	                user.getUserId(),
	                user.getUserName(),
	                user.getEmail(),
	                user.isAccountNonLocked(),
	                user.isAccountNonExpired(),
	                user.isCredentialsNonExpired(),
	                user.isEnabled(),
	                user.getCredentialsExpiryDate(),
	                user.getAccountExpiryDate(),
	                user.isTwoFactorEnabled(),
	                strRoles
	        );

	        return ResponseEntity.ok().body(response);
	}
	 @GetMapping("/username")
	    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
	        return (userDetails != null) ? userDetails.getUsername() : "";
	    }
}
