package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;

public record CreateFollowerDto(AppUser Follower, AppUser Followee) {
}
