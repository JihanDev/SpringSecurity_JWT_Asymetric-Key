package org.sid.securityservice.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {
    private final AccountAuthenticationProvider authenticationProvider;
    private final RsaKeysConfig rsaKeysConfig;
    private PasswordEncoder passwordEncoder;

    public WebSecurityConfiguration(AccountAuthenticationProvider authenticationProvider, RsaKeysConfig rsaKeysConfig, PasswordEncoder passwordEncoder) {
        this.authenticationProvider = authenticationProvider;
        this.rsaKeysConfig = rsaKeysConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        var authProvider= new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     return http
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests(auth->auth
                    .requestMatchers("/token/**").permitAll()
                    .requestMatchers("/register/**").permitAll()
                    .requestMatchers("/listUsers/**").permitAll()
                    .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider)
            .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .httpBasic(Customizer.withDefaults())
            .headers(headers->headers.frameOptions().sameOrigin())
            .build();
}
    @Bean
    JwtDecoder jwtDecoder(){
    return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk=new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource=new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

}

