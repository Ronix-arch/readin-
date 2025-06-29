package oth.ics.wtp.readinbackend.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.services.AppUserService;
import oth.ics.wtp.readinbackend.services.AuthService;

import java.util.List;

@RestController
public class AppUserController {
    private final AuthService authService;
    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AuthService authService, AppUserService appUserService) {
        this.authService = authService;
        this.appUserService = appUserService;
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping(value = "appUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserDto> getAppUsers(HttpServletRequest request) {
        authService.getAuthenticatedUser(request);
        return appUserService.appUsersList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "appUsers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto createAppUser(@RequestBody CreateAppUserDto createAppUser) {
        return appUserService.create(createAppUser);
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping(value = "appUsers/{appUserName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto getAppUser(HttpServletRequest request, @PathVariable("appUserName") String appUserName) {
        authService.getAuthenticatedUser(request);
        return appUserService.get(appUserName);

    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "appUsers/{appUserName}")
    public void deleteAppUser(HttpServletRequest request, @PathVariable("appUserName") String appUserName) {
        authService.getAuthenticatedUser(request);
        appUserService.delete(appUserName);
    }


    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value = "appUsers/login")
    public AppUserDto logIn(HttpServletRequest request) {
        AppUser appUser = authService.logIn(request);
        return appUserService.get(appUser.getName());
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "appUsers/logout")
    public void logOut(HttpServletRequest request) {
        authService.logOut(request);
    }

}
