package net.java.SecureNotes.securityConfig.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class userInfoResponse {

	    private Long id;
	    private String username;
	    private String email;
	    private boolean accountNonLocked;
	    private boolean accountNonExpired;
	    private boolean credentialsNonExpired;
	    private boolean enabled;
	    private LocalDate credentialsExpiryDate;
	    private LocalDate accountExpiryDate;
	    private boolean isTwoFactorEnabled;
	    private List<String> roles;
}
