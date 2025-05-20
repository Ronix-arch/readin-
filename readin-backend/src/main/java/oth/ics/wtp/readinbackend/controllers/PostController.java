package oth.ics.wtp.readinbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import oth.ics.wtp.readinbackend.dtos.CreatePostDto;
import oth.ics.wtp.readinbackend.dtos.PostDto;
import oth.ics.wtp.readinbackend.dtos.UpdatePostDto;
import oth.ics.wtp.readinbackend.services.AuthService;
import oth.ics.wtp.readinbackend.services.PostService;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController public class PostController {
   private final AuthService authService;
   private final PostService postService;

   @Autowired public PostController(AuthService authService, PostService postService) {
       this.authService = authService;
       this.postService = postService;
   }

   @GetMapping(value = "appUsers/{appUserId}/posts/ownPosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getOwnPosts(HttpServletRequest request, @PathVariable("appUserId") long appUserId) {
       authService.getAuthenticatedUser(request);
       return postService.getUserOwnPosts(appUserId);
   }
@GetMapping(value = "appUsers/{appUserId}/posts/timeLinePosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getTimeLinePosts(HttpServletRequest request, @PathVariable("appUserId") long appUserId) {
       authService.getAuthenticatedUser(request);
       return postService.getUserTimeLinePosts(appUserId);
}
@PostMapping(value ="appUsers/{appUserId}/posts",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public PostDto createPost(HttpServletRequest request, @PathVariable("appUserId") long appUserId, @RequestBody CreatePostDto createPostDto) {
       authService.getAuthenticatedUser(request);
       return postService.createPost(appUserId, createPostDto);
}
@PutMapping(value = "appUsers/{appUserId}/posts/{postId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto updatePost(HttpServletRequest request,@PathVariable("appUserId") long appUserId, @PathVariable("postId") long postId, @RequestBody UpdatePostDto updatePostDto) {
       authService.getAuthenticatedUser(request);
       return postService.updatePost(appUserId, postId, updatePostDto);
}
@ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "appUsers/{appUserId}/posts/{postId}")
    public void deletePost(HttpServletRequest request,@PathVariable("appUserId") long appUserId, @PathVariable("postId") long postId) {
       authService.getAuthenticatedUser(request);
       postService.deletePost(appUserId, postId);
}

}
