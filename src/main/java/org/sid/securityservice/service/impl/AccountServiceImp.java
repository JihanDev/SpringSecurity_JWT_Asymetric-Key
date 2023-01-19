package org.sid.securityservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.sid.securityservice.model.AppRole;
import org.sid.securityservice.model.AppUser;
import org.sid.securityservice.repository.AppRoleRepository;
import org.sid.securityservice.repository.AppUserRepository;
import org.sid.securityservice.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder encoder;
    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        AppRole role=appRoleRepository.findByRoleName("USER");
        Set<AppRole> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return appUserRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser findByUserName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser addRoleToUser(String username, String roleName) {

        AppUser user=appUserRepository.findByUsername(username);
        Set<AppRole> roles=user.getRoles();
        AppRole role=appRoleRepository.findByRoleName(roleName);
        roles.add(role);
        user.setRoles(roles);
        appUserRepository.save(user);
        return user;
    }
}
