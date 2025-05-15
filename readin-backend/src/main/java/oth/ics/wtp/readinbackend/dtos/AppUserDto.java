package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.Follower;

import java.time.Instant;
import java.util.List;

public record AppUserDto(long id, String name, String password, Instant createdAt, List<Follower> followers, List<Follower> following) {
}
