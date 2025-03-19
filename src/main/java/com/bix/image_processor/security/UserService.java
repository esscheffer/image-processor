package com.bix.image_processor.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }
}
