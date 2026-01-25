package oth.ics.wtp.readinbackend.dtos;

import java.time.Instant;
import oth.ics.wtp.readinbackend.entities.UserRole;

public record AppUserDto(
        Long id,
        String name,
        Instant createdAt,
        String profilePictureUrl,
        String email,
        String bio,
        UserRole role
) {
}
