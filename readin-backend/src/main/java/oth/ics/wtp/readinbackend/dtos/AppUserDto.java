package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;

public record AppUserDto(
        Long id,
        String name,
        Instant createdAt,
        String profilePictureUrl,
        String email,
        String bio
) {
}
