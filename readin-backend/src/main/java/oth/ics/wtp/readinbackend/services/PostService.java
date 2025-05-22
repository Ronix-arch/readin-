package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.dtos.CreatePostDto;
import oth.ics.wtp.readinbackend.dtos.PostDto;
import oth.ics.wtp.readinbackend.dtos.UpdatePostDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Post;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.repositories.FollowingRepository;
import oth.ics.wtp.readinbackend.repositories.PostRepository;

import java.util.List;

@Service public class PostService {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private  final FollowingRepository followingRepository;

    @Autowired
    public PostService(PostRepository postRepository, AppUserRepository appUserRepository, FollowingRepository followingRepository) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
        this.followingRepository = followingRepository;
    }

    public List<PostDto> getUserOwnPosts(long userId) {
        if (!appUserRepository.findById(userId).isPresent()) {
            throw ClientErrors.userIdNotFound(userId);  // this another method
        }

        return postRepository.findByAppUser_IdOrderByCreatedAtDesc(userId).stream().map(this::toDto).toList();

    }
    public List<PostDto> getUserTimeLinePosts(long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        return postRepository.findTimelinePosts(userId).stream().map(this::toDto).toList();
    }
    public PostDto createPost(long userId, CreatePostDto createPostDto) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(()-> ClientErrors.userIdNotFound(userId));
        Post entity = toEntity(appUser, createPostDto);
        postRepository.save(entity);
        return toDto(entity);


    }
    public PostDto updatePost(long userId, long postId, UpdatePostDto updatePostDto) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        Post post = postRepository.findByAppUser_IdAndId(userId,postId).orElseThrow(()-> ClientErrors.postNotFound(postId));
        post.setContent(updatePostDto.content());
        return toDto(postRepository.save(post));

    }
    public void deletePost(long userId, long postId) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        if(!postRepository.existsByAppUser_IdAndId(userId,postId)) {
            throw ClientErrors.postNotFound(postId);
        }
        postRepository.deleteById(postId);
    }

    private Post toEntity(AppUser appUser, CreatePostDto createPostDto) {
return new Post(createPostDto.content(),appUser);
    }

    private PostDto toDto(Post post) {
        return new PostDto(post.getId(),post.getContent(),post.getCreatedAt(),post.getAppUser().getId());
    }


}
