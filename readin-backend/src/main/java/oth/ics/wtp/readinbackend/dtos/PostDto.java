package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;


public record PostDto(long id, String content, Instant createdAt, long userId, String userName) {
}
