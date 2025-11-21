package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;


public record PostDto(long id, String content, String attachmentUrl, String attachmentType, Instant createdAt, long userId, String userName) {
}
