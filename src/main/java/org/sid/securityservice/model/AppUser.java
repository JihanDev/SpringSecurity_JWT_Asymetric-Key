package org.sid.securityservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private  String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  String password;
    private boolean activated;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    //do this at seperate table
    @JoinTable(name = "UserRole",
            joinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId",referencedColumnName = "id"))
    private Set<AppRole> roles= new HashSet<>();

}
