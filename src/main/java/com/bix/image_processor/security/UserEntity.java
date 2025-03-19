package com.bix.image_processor.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String email;
    private Long quota;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case SIMPLE -> List.of(new SimpleGrantedAuthority("SIMPLE"));
            case PREMIUM -> List.of(new SimpleGrantedAuthority("PREMIUM"));
        };
    }
}
