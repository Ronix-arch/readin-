package oth.ics.wtp.readinbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.UserRole;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;

@SpringBootApplication
public class ReadinBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadinBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!appUserRepository.existsByName("admin")) {
                AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);
                appUserRepository.save(admin);
            }
        };
    }
}
