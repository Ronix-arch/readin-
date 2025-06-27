package oth.ics.wtp.readinbackend.dtos;

import oth.ics.wtp.readinbackend.entities.AppUser;

import java.time.Instant;
                                                              //, AppUser user?
public record PostDto(long id , String content, Instant createdAt, long userId,String userName) {
}
