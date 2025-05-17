package oth.ics.wtp.readinbackend.services;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.WeakCrypto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@Service
public class AuthService {
    private static final String  SESSION_USER_NAME = "readin-session-user-name";
    private final AppUserRepository appUserRepository;

    @Autowired
    public AuthService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    public AppUser getAuthenticatedUser(HttpServletRequest request) {
        Object sessionUserName = request.getSession().getAttribute(SESSION_USER_NAME);
        if (sessionUserName instanceof String userName) {
            Optional<AppUser> appUser = appUserRepository.findByName(userName);
            if (appUser.isPresent()) {
                return appUser.get();
            } else {
                logOut(request);
                throw ClientErrors.unauthorized();
            }

        }
        return logIn(request);
    }
    private AppUser logIn(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String decoded = WeakCrypto.base64decode(authHeader.substring(authheader.index(" ")+1));
            String [] parts = decoded.split(":");
            String username = parts[0];
            String password = parts[1];
            String hashedPassword = WeakCrypto.hashPassword(password);
            AppUser appUser = appUserRepository.findByName(username).orElseThrow();
            if (!appUser.getHashedPassword().equals(hashedPassword)) {
                throw new Exception();
            }
            request.getSession().setAttribute(SESSION_USER_NAME, username);
            return appUser;
        } catch (Exception e){
            logOut(request);
            throw ClientErrors.unauthorized();
        }

    }
    private void logOut(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_USER_NAME, null);
    }

}
