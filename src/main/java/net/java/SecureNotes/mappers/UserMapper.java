package net.java.SecureNotes.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.java.SecureNotes.dtoClasses.UserDto;
import net.java.SecureNotes.entity.User;

public class UserMapper {
   public static UserDto convertToDto(User user) {

	  return new UserDto( 
			  user.getUserId(),
              user.getUserName(),
              user.getEmail(),
              user.isAccountNonLocked(),
              user.isAccountNonExpired(),
              user.isCredentialsNonExpired(),
              user.isEnabled(),
              user.getCredentialsExpiryDate(),
              user.getAccountExpiryDate(),
              user.getTwoFactorSecret(),
              user.isTwoFactorEnabled(),
              user.getSignUpMethod(),
              user.getRole(),
              user.getCreatedDate(),
              user.getUpdatedDate()
              );
  }
	
}
