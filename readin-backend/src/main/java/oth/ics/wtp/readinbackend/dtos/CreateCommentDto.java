package oth.ics.wtp.readinbackend.dtos;

public record CreateCommentDto(
    String content,
    Long parentCommentId
) {
}
