package oth.ics.wtp.readinbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.services.AuthService;
import oth.ics.wtp.readinbackend.services.FollowingService;

import java.util.List;

@SecurityRequirement(name ="basicAuth")
@RestController
public class FollowingController {
    private final AuthService authService;
    private final FollowingService followingService;

    public FollowingController(AuthService authService, FollowingService followingService) {
        this.authService = authService;
        this.followingService = followingService;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "appUsers/{followerId}/following/{followeeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void followUser(HttpServletRequest request, @PathVariable("followerId") long followerId, @PathVariable("followeeId") long followeeId) {
        authService.getAuthenticatedUser(request);
        followingService.followUser(followerId, followeeId);
    }
    @DeleteMapping(value = "appUsers/{followerId}/following/{followeeId}")
    public void unfollowUser(HttpServletRequest request,@PathVariable("followerId") long followerId, @PathVariable("followeeId") long followeeId) {
        authService.getAuthenticatedUser(request);
        followingService.unfollowUser(followerId, followeeId);
    }
    @GetMapping(value = "appUsers/{followerId}/following/{followeeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isFollowing(HttpServletRequest request,@PathVariable long followerId, @PathVariable long followeeId) {
        authService.getAuthenticatedUser(request);
        return followingService.isFollowing(followerId, followeeId);
    }
    @GetMapping(value = "appUsers/{appUserId}/following/followers",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserDto> getFollowers(HttpServletRequest request, @PathVariable long appUserId) {
       authService.getAuthenticatedUser(request);
       return followingService.getFollowers(appUserId);
    }

    @GetMapping(value = "appUsers/{appUserId}/following/followees",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserDto> getFollowees(HttpServletRequest request, @PathVariable long appUserId) {
        authService.getAuthenticatedUser(request);
        return followingService.getFollowings(appUserId);
    }


}
