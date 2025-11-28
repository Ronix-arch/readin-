package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;
import oth.ics.wtp.readinbackend.entities.Comment;

public record CommentDto(
    Long id,
    String content,
    Instant createdAt,
    Long authorId,
    String authorName,
    Long postId,
    Long parentCommentId,
    int replyCount
) {
    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getAuthor().getId(),
            comment.getAuthor().getName(),
            comment.getPost().getId(),
            comment.getParentComment() != null ? comment.getParentComment().getId() : null,
            comment.getReplies() != null ? comment.getReplies().size() : 0
        );
    }
}
