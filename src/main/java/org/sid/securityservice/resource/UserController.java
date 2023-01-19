package org.sid.securityservice.resource;

import lombok.RequiredArgsConstructor;
import org.sid.securityservice.model.AppUser;
import org.sid.securityservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class UserController {
    private final AccountService accountService;
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user){
        AppUser newUser=accountService.saveUser(user);
        return ResponseEntity.created(getLocation(newUser.getId().intValue())).body(newUser);

    }

    @GetMapping("/listUsers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok(accountService.listUsers());
    }
    //protected static to be used in the same package
    protected static URI getLocation(Integer id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(id).toUri();
    }

    }
