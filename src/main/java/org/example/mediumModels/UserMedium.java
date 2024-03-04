package org.example.mediumModels;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "get.user.by.email.password" , query = "select u from UserMedium u where u.email=:email")
})
@ToString

public class UserMedium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Role> role;

}
