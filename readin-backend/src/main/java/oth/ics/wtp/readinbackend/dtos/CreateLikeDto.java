package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Post;


public record CreateLikeDto(AppUser user, Post post) {
}
