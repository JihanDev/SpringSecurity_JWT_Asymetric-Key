package org.sid.securityservice.resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final  UserDetailsService userDetailsService;
    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping( "/token")
    public ResponseEntity<Map<String,String>> jwtToken(String username,String password,boolean withRefreshToken,String grantType,String refreshToken){
        String subject=null;
        String scope=null;
        if(grantType.equals("password")){
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            subject=authentication.getName();
            scope=authentication.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.joining(" "));

        }else if (grantType.equals("refreshToken")) {
            if (refreshToken == null) {
                return new ResponseEntity<>(Map.of("errorMessage", "refresh token is required"), HttpStatus.UNAUTHORIZED);
            }
            Jwt jwtDecoded = null;
            try {
                jwtDecoded = jwtDecoder.decode(refreshToken);
            } catch (JwtException e) {
                return new ResponseEntity<>(Map.of("errorMessage", e.getMessage()), HttpStatus.UNAUTHORIZED);
            }
            subject =jwtDecoded.getSubject();
            UserDetails userDetails=userDetailsService.loadUserByUsername(subject);
            Collection<?extends GrantedAuthority> authorities=userDetails.getAuthorities();
            scope=authorities.stream().map(auth->auth.getAuthority()).collect(Collectors.joining(" "));
        }
        Map<String,String> idToken= new HashMap<>();
        Instant instant=Instant.now();
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .subject(subject)
                .claim("scope",scope)
                .issuedAt(instant)
                .expiresAt(instant.plus(1, ChronoUnit.MINUTES))
                .issuer("Security-service")
                .build();
        String jwtAccessToken=jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        idToken.put("access-Token",jwtAccessToken);
        if(withRefreshToken){
            JwtClaimsSet jwtClaimsSetRefresh=JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(5,ChronoUnit.MINUTES))
                    .issuer("Security-service")
                    .build();
            String jwtRefreshToken=jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();
            idToken.put("refresh-token",jwtRefreshToken);
        }
        return new ResponseEntity<>(idToken, HttpStatus.OK);
    }
}

