package net.java.SecureNotes.securityConfig.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Signup {
    @NotBlank
    @Size(min=3,max=20)
	private String username;
    @NotBlank
    @Size(max=50)
    @Email
	private String email;
	private Set<String> role;
	@NotBlank
	@Size(min=6,max=30)
	private String password;
	
}
