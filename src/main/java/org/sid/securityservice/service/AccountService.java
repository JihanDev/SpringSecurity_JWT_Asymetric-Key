package org.sid.securityservice.service;

import org.sid.securityservice.model.AppRole;
import org.sid.securityservice.model.AppUser;

import java.util.List;

public interface AccountService {
    public AppUser saveUser(AppUser user);
    public AppRole saveRole(AppRole role);
    public AppUser findByUserName(String username);
    public List<AppUser> listUsers();
    public AppUser  addRoleToUser(String username,String roleName);

}
