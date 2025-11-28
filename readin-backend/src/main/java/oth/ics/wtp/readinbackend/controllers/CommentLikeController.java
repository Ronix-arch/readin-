package oth.ics.wtp.readinbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.services.AuthService;
import oth.ics.wtp.readinbackend.services.CommentLikeService;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/comments/{commentId}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final AuthService authService;

    @Autowired
    public CommentLikeController(CommentLikeService commentLikeService, AuthService authService) {
        this.commentLikeService = commentLikeService;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeComment(HttpServletRequest request, @PathVariable Long commentId) {
        AppUser user = authService.getAuthenticatedUser(request);
        commentLikeService.likeComment(commentId, user.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeComment(HttpServletRequest request, @PathVariable Long commentId) {
        AppUser user = authService.getAuthenticatedUser(request);
        commentLikeService.unlikeComment(commentId, user.getId());
    }

    @GetMapping("/status")
    public boolean hasUserLikedComment(HttpServletRequest request, @PathVariable Long commentId) {
        AppUser user = authService.getAuthenticatedUser(request);
        return commentLikeService.hasUserLikedComment(commentId, user.getId());
    }

    @GetMapping("/count")
    public long getLikeCount(@PathVariable Long commentId) {
        return commentLikeService.getLikeCount(commentId);
    }
}
