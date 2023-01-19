package org.sid.securityservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.sid.securityservice.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountServiceImp accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user=accountService.findByUserName(username);
        if(user == null){
            throw  new UsernameNotFoundException("User "+username+" not found");
        }
        if(user.getRoles() == null){
            throw new RuntimeException(" user has no roles");
        }
        Collection<GrantedAuthority> authorities=user.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
        return new User(user.getUsername(),user.getPassword(),authorities);
    }
}
