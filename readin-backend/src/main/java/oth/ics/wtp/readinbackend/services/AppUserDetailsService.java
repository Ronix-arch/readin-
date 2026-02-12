package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;

import java.util.Collections;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByName(username)
                .map(u -> new User(
                        u.getName(),
                        u.getPassword(),
                        Collections.emptyList() // Roles would go here if needed
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
