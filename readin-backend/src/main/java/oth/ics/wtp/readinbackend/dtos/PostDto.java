package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;

import java.time.Instant;

public record PostDto(long id , String content, Instant createdAt, AppUser user) {
}
