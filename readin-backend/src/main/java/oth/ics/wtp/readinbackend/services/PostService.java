package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final FollowingRepository followingRepository;
    private final StorageService storageService;

    @Autowired
    public PostService(PostRepository postRepository, AppUserRepository appUserRepository, FollowingRepository followingRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
        this.followingRepository = followingRepository;
        this.storageService = storageService;
    }

    public Page<PostDto> getUserOwnPosts(long userId, Pageable pageable) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);  // this another method
        }

//        return postRepository.findByAppUser_IdOrderByCreatedAtDesc(userId).stream().map(this::toDto).toList();

        return postRepository
                .findByAppUser_IdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toDto);

    }

    public Page<PostDto> getUserTimeLinePosts(long userId, Pageable pageable) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        return postRepository
                .findTimelinePosts(userId, pageable)
                .map(this::toDto); // Page.map() works directly on Page<Post>
    }

    public PostDto createPost(long userId, String content, MultipartFile file) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() -> ClientErrors.userIdNotFound(userId));
        Post entity = new Post(content, appUser);
        if (file != null && !file.isEmpty()) {
            String filename = storageService.store(file);
            entity.setAttachmentUrl(filename);
            entity.setAttachmentType(file.getContentType());
        }
        postRepository.save(entity);
        return toDto(entity);
    }

    public PostDto updatePost(long userId, long postId, UpdatePostDto updatePostDto) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        Post post = postRepository.findByAppUser_IdAndId(userId, postId).orElseThrow(() -> ClientErrors.postNotFound(postId));
        post.setContent(updatePostDto.content());
        return toDto(postRepository.save(post));

    }

    public void deletePost(long userId, long postId) {
        if (!appUserRepository.existsById(userId)) {
            throw ClientErrors.userIdNotFound(userId);
        }
        if (!postRepository.existsByAppUser_IdAndId(userId, postId)) {
            throw ClientErrors.postNotFound(postId);
        }
        postRepository.deleteById(postId);
    }

    private PostDto toDto(Post post) {
        return new PostDto(post.getId(), post.getContent(), post.getAttachmentUrl(), post.getAttachmentType(), post.getCreatedAt(), post.getAppUser().getId(), post.getAppUser().getName());
    }


}
