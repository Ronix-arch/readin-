package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Post;

import java.time.Instant;

public record LikeDto(long id, AppUser user, Post post, Instant createdAt) {
}
