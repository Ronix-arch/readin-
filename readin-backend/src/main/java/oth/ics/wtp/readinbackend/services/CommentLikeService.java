package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Comment;
import oth.ics.wtp.readinbackend.entities.CommentLike;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.repositories.CommentLikeRepository;
import oth.ics.wtp.readinbackend.repositories.CommentRepository;

@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final AppUserRepository appUserRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository, AppUserRepository appUserRepository, CommentRepository commentRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.appUserRepository = appUserRepository;
        this.commentRepository = commentRepository;
    }

    public void likeComment(Long commentId, Long userId) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> ClientErrors.userIdNotFound(userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (commentLikeRepository.findByAppUserIdAndCommentId(userId, commentId).isPresent()) {
            throw new IllegalArgumentException("You have already liked this comment");
        }

        CommentLike commentLike = new CommentLike(appUser, comment);
        commentLikeRepository.save(commentLike);
    }

    public void unlikeComment(Long commentId, Long userId) {
        CommentLike commentLike = commentLikeRepository.findByAppUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("You have not liked this comment"));
        commentLikeRepository.delete(commentLike);
    }

    public boolean hasUserLikedComment(Long commentId, Long userId) {
        return commentLikeRepository.findByAppUserIdAndCommentId(userId, commentId).isPresent();
    }

    public long getLikeCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }
}
