package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;

import java.time.Instant;

public record FollowerDto(long id, AppUser Follower, AppUser Followee, Instant createdAt) {
}
