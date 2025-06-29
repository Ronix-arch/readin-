package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;

public record AppUserDto(long id, String name, Instant createdAt) {
}
