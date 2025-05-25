package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Following;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.repositories.FollowingRepository;

import java.util.List;

@Service public class FollowingService {
 private FollowingRepository followingRepository;
 private AppUserRepository appUserRepository;

 @Autowired public FollowingService(FollowingRepository followingRepository, AppUserRepository appUserRepository) {
     this.followingRepository = followingRepository;
     this.appUserRepository = appUserRepository;
 }
    public void followUser(long followerId, long  followeeId) {
        if (followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw ClientErrors.followingAlreadyExsists(followerId,followeeId);
        }
            AppUser follower = appUserRepository.findById(followerId).orElseThrow(()-> ClientErrors.userIdNotFound(followerId));
            AppUser followee = appUserRepository.findById(followeeId).orElseThrow(()-> ClientErrors.userIdNotFound(followeeId));
            Following following = toEntity(follower,followee);
            followingRepository.save(following);

    }
    @Transactional
    public void unfollowUser(long followerId, long  followeeId) {
        if (!followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw ClientErrors.followingDoesNotExsists(followerId,followeeId);
        }

        followingRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }
    public boolean isFollowing(long followerId, long  followeeId) {
        return followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }
    public List<AppUserDto> getFollowers(long userId) {
        List<Following> followers = followingRepository.findByFolloweeId(userId);
        return followers.stream()
                .map(f -> appUserRepository.findById(f.getFollower().getId())
                        .map(this::convertToDto)
                        .orElseThrow(()-> ClientErrors.userIdNotFound(f.getFollower().getId())))
                .toList();

                //.filter(Objects::nonNull)
                //.collect(Collectors.toList());
    }

    public List<AppUserDto> getFollowings(long userId) {
        List<Following> followings = followingRepository.findByFollowerId(userId);
        return followings.stream()
                .map(f -> appUserRepository.findById(f.getFollowee().getId())
                        .map(this::convertToDto)
                        .orElseThrow(()-> ClientErrors.userIdNotFound(f.getFollower().getId())))
                .toList();



    }
    private AppUserDto convertToDto(AppUser appUser) {
        return new AppUserDto(appUser.getId(),appUser.getName(),appUser.getCreatedAt(),appUser.getFollowers(),appUser.getFollowings());
    }
    private Following toEntity(AppUser follower, AppUser followee) {
     return new Following(follower,followee);
    }

}
