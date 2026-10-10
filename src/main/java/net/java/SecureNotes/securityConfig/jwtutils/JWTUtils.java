package net.java.SecureNotes.securityConfig.jwtutils;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Component
@Data
public class JWTUtils {
 
	private final Logger loggerHandler=LoggerFactory.getLogger(JWTUtils.class);
	
	@Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    
    public String getJwtFromHeader(HttpServletRequest request){
    	String bearerToken=request.getHeader("Authorization");
    	loggerHandler.info("Authorization header",bearerToken);
    	if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
    		return bearerToken.substring(7);
    	}
    	return null;
    }
    
    public String generateJwt(UserDetails userdetails) {
    	 String userName=userdetails.getUsername();
    	 return Jwts.builder()
    			 .setSubject(userName)
    			 .setIssuedAt(new Date())
    			 .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
    			 .signWith(key())
    			 .compact();
    }
    
    public String getUserNamefromJwt(String token) {
    	  return Jwts.parserBuilder()
                  .setSigningKey(key())
                  .build()
                  .parseClaimsJws(token)
                  .getBody()
                  .getSubject();
    }
    
    public boolean ValidateJwtToken(String token) {
    	try {
			loggerHandler.info("Validate");
            Jwts.parserBuilder().
            setSigningKey(key()).
            build().
            parseClaimsJws(token);
			return true;
    	  } catch (MalformedJwtException e) {
    		  loggerHandler.error("Invalid JWT token: {}", e.getMessage());
          } catch (ExpiredJwtException e) {
        	  loggerHandler.error("JWT token is expired: {}", e.getMessage());
          } catch (UnsupportedJwtException e) {
        	  loggerHandler.error("JWT token is unsupported: {}", e.getMessage());
          } catch (IllegalArgumentException e) {
        	  loggerHandler.error("JWT claims string is empty: {}", e.getMessage());
          }
    	return false;
    }
}
