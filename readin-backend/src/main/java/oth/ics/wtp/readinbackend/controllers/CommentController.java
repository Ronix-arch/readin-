package oth.ics.wtp.readinbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.dtos.CommentDto;
import oth.ics.wtp.readinbackend.dtos.CreateCommentDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.services.AuthService;
import oth.ics.wtp.readinbackend.services.CommentService;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;

    @Autowired
    public CommentController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommentDto> getCommentsByPost(@PathVariable Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getCommentsByPost(postId, pageable);
    }

    @GetMapping(value = "/{commentId}/replies", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommentDto> getReplies(@PathVariable Long commentId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getReplies(commentId, pageable);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(HttpServletRequest request, @PathVariable Long postId, @RequestBody CreateCommentDto createCommentDto) {
        AppUser author = authService.getAuthenticatedUser(request);
        return commentService.createComment(postId, author.getId(), createCommentDto);
    }
}
