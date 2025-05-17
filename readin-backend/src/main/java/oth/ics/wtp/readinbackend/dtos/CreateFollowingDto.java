package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;

public record CreateFollowingDto(AppUser Follower, AppUser Followee) {
}
