package oth.ics.wtp.readinbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.services.AuthService;
import oth.ics.wtp.readinbackend.services.LikeService;

@SecurityRequirement(name ="basicAuth")
@RestController public class LikeController {
    private final AuthService authService;
    private final LikeService likeService;

   @Autowired      // why here and not in app user
   public LikeController(AuthService authService, LikeService likeService) {
        this.authService = authService;
        this.likeService = likeService;
    }



    @ResponseStatus(HttpStatus.CREATED)
@PostMapping(value = "appUsers/{appUserId}/posts/{postId}/like",produces = MediaType.APPLICATION_JSON_VALUE)
    public void likePost(HttpServletRequest request, @PathVariable("appUserId") long appUserId, @PathVariable("postId") long postId) {
        authService.getAuthenticatedUser(request);
        likeService.likePost(appUserId, postId);

}

@ResponseStatus(HttpStatus.NO_CONTENT)
@DeleteMapping(value = "appUsers/{appUserId}/posts/{postId}/like")
    public void unlikePost(HttpServletRequest request, @PathVariable("appUserId") long appUserId, @PathVariable("postId") long postId) {
        authService.getAuthenticatedUser(request);
        likeService.unlikePost(appUserId, postId);
}
@GetMapping(value = "appUsers/{appUserId}/posts/{postId}/like",produces = MediaType.APPLICATION_JSON_VALUE)
    public  boolean hasUserLikedPost(HttpServletRequest request,@PathVariable("appUserId") long appUserId, @PathVariable("postId") long postId) {
        authService.getAuthenticatedUser(request);
        return likeService.hasUserLikedPost(appUserId, postId);
}
    @GetMapping(value = "appUsers/posts/{postId}/likeCount",produces = MediaType.APPLICATION_JSON_VALUE)
    public int getLikeCount(HttpServletRequest request,long appUserId, @PathVariable("postId") long postId) {
        authService.getAuthenticatedUser(request); // do not know if this is needed
        return likeService.getLikeCount(postId);
    }

}
