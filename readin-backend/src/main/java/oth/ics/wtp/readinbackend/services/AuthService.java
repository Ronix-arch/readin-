package oth.ics.wtp.readinbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.UserRole;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AuthService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser getAuthenticatedUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            return appUserRepository.findByName(authentication.getName())
                    .orElseThrow(ClientErrors::unauthorized);
        }
        throw ClientErrors.unauthorized();
    }

    public AppUser logIn(HttpServletRequest request) {
        // With Spring Security HTTP Basic, if we reach here, the user is already
        // authenticated.
        // We just need to return the user object.
        return getAuthenticatedUser(request);
    }

    public void logOut(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAdmin(AppUser user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }
}
