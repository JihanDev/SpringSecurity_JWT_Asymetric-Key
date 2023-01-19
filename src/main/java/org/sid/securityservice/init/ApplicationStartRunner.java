package org.sid.securityservice.init;



import lombok.RequiredArgsConstructor;
import org.sid.securityservice.model.AppRole;
import org.sid.securityservice.model.AppUser;
import org.sid.securityservice.repository.AppRoleRepository;
import org.sid.securityservice.repository.AppUserRepository;
import org.sid.securityservice.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class ApplicationStartRunner implements CommandLineRunner {
    private final AppRoleRepository roleRepository;
   private final AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        AppRole roleUser = new AppRole(1L, "USER");
        AppRole roleAdmin = new AppRole(2L,"ADMIN");
        roleRepository.saveAll(asList(roleUser, roleAdmin));
        Set<AppRole>roles=new HashSet<>();
        AppUser user1= new AppUser(1L,"Jihane","1234",true,roles);
        AppUser user2= new AppUser(2L,"Imane","1234",true,roles);
        accountService.saveUser(user1);
        accountService.saveUser(user2);

       accountService.addRoleToUser("Jihane","ADMIN");


    }
}

