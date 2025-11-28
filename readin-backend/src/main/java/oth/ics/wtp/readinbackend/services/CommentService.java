package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.dtos.CommentDto;
import oth.ics.wtp.readinbackend.dtos.CreateCommentDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Comment;
import oth.ics.wtp.readinbackend.entities.Post;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.repositories.CommentRepository;
import oth.ics.wtp.readinbackend.repositories.PostRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, AppUserRepository appUserRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
    }

    public Page<CommentDto> getCommentsByPost(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(postId, pageable)
                .map(CommentDto::fromEntity);
    }

    public Page<CommentDto> getReplies(Long commentId, Pageable pageable) {
        return commentRepository.findByParentCommentIdOrderByCreatedAtDesc(commentId, pageable)
                .map(CommentDto::fromEntity);
    }

    public CommentDto createComment(Long postId, Long authorId, CreateCommentDto createCommentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ClientErrors.postNotFound(postId));
        AppUser author = appUserRepository.findById(authorId)
                .orElseThrow(() -> ClientErrors.userIdNotFound(authorId));

        Comment parentComment = null;
        if (createCommentDto.parentCommentId() != null) {
            parentComment = commentRepository.findById(createCommentDto.parentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(createCommentDto.content(), author, post, parentComment);
        Comment savedComment = commentRepository.save(comment);
        return CommentDto.fromEntity(savedComment);
    }
}
