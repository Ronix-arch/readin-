package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.readinbackend.entities.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
   Optional< AppUser> findByName(String name);
   boolean existsByName(String username);
}
