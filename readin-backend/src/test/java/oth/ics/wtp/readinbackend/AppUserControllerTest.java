package oth.ics.wtp.readinbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.readinbackend.controllers.AppUserController;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.services.AppUserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppUserControllerTest extends ReadinControllerTestBase {
    @Autowired
    private AppUserController appUserController;
    @Autowired
    private AppUserService appUserService;


    @Test
    public void testCreateList() {
        appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
        appUserController.createAppUser(new CreateAppUserDto("user2", "password2"));

        List<AppUserDto> appUsers = appUserController.getAppUsers(user0(), null);
        assertTrue(appUsers.stream().anyMatch(u -> u.name().equals("user1")));
        assertTrue(appUsers.stream().anyMatch(u -> u.name().equals("user2")));

    }

    @Test
    public void testCreateLoginLogout() {
        appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
        assertDoesNotThrow(() -> appUserController.logIn(mockRequest("user1", "password1")));
        assertDoesNotThrow(() -> appUserController.logOut(mockRequest("user1", "password1")));
    }

    @Test
    public void testCreateGetDelete() {
        AppUserDto user1 = appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
        appUserController.createAppUser(new CreateAppUserDto("user2", "password2"));
        AppUserDto appUser = appUserController.getAppUser(user0(), user1.id());
        assertEquals("user1", appUser.name());
        assertThrows(ResponseStatusException.class, () -> appUserController.getAppUser(mockRequest("userNon", "notAuthenticated"), user1.id()));// testing the authservice
        appUserController.deleteAppUser(user0(), "user1");
        assertThrows(ResponseStatusException.class, () -> appUserController.getAppUser(user0(), user1.id()));


    }


}
