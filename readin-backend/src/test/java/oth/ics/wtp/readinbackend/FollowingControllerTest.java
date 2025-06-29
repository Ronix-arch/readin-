package oth.ics.wtp.readinbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.readinbackend.controllers.AppUserController;
import oth.ics.wtp.readinbackend.controllers.FollowingController;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FollowingControllerTest extends ReadinControllerTestBase {
    @Autowired
    private AppUserController appUserController;
    @Autowired
    private FollowingController followingController;

    private long followerId1;
    private long followerId2;
    private long followeeId1;
    private long followeeId2;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        followerId1 = appUserController.createAppUser(new CreateAppUserDto("follower1", "password1")).id();
        followerId2 = appUserController.createAppUser(new CreateAppUserDto("follower2", "password2")).id();
        followeeId1 = appUserController.createAppUser(new CreateAppUserDto("followee1", "password3")).id();
        followeeId2 = appUserController.createAppUser(new CreateAppUserDto("followee2", "password4")).id();

        followingController.followUser(user0(), followerId1, followeeId1);
        followingController.followUser(user0(), followerId1, followeeId2);

        followingController.followUser(user0(), followerId2, followeeId1);
        followingController.followUser(user0(), followerId2, followeeId2);

    }

    @Test
    public void testListfollowuser() {

        List<AppUserDto> followersOfFollowee1 = followingController.getFollowers(user0(), followeeId1);
        List<AppUserDto> followeesOfFollower1 = followingController.getFollowees(user0(), followerId1);
        assertEquals(2, followersOfFollowee1.size());
        assertEquals(2, followeesOfFollower1.size());
        assertTrue(followeesOfFollower1.stream().anyMatch(p -> p.name().equals("followee1")));
        assertTrue(followersOfFollowee1.stream().anyMatch(p -> p.name().equals("follower1")));
    }

    @Test
    public void testUnfollowUser() {
        assertTrue(followingController.isFollowing(user0(), followerId1, followeeId1));
        followingController.unfollowUser(user0(), followerId1, followeeId1);
        assertFalse(followingController.isFollowing(user0(), followerId1, followeeId1));
        assertThrows(ResponseStatusException.class, () -> followingController.unfollowUser(user0(), followerId2, 34));
    }


}

