package oth.ics.wtp.readinbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.readinbackend.controllers.AppUserController;
import oth.ics.wtp.readinbackend.controllers.LikeController;
import oth.ics.wtp.readinbackend.controllers.PostController;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreatePostDto;

import static org.junit.jupiter.api.Assertions.*;

public class LikeControllerTest extends ReadinControllerTestBase {
    @Autowired
    private PostController postController;
    @Autowired
    private LikeController likeController;
    @Autowired
    private AppUserController appUserController;

    private long appUserId;
    private long likingUserId1;
    private long likingUserId2;
    private long postId;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        appUserId = appUserController.createAppUser(new CreateAppUserDto("user1", "password1")).id();
        likingUserId1 = appUserController.createAppUser(new CreateAppUserDto("user2", "password2")).id();
        likingUserId2 = appUserController.createAppUser(new CreateAppUserDto("user3", "password3")).id();
        postId = postController.createPost(user0(), appUserId, new CreatePostDto("post1OfUser1")).id();

    }

    @Test
    public void testlikeAndgetlikes() {
        likeController.likePost(user0(), likingUserId1, postId);
        likeController.likePost(user0(), likingUserId2, postId);
        long NoOfLikes = likeController.getLikeCount(user0(), postId);
        assertEquals(2, NoOfLikes);
        assertThrows(ResponseStatusException.class, () -> likeController.likePost(user0(), likingUserId1, postId));

    }

    @Test
    public void testunlikePostCheck() {
        likeController.likePost(user0(), likingUserId1, postId);
        likeController.likePost(user0(), likingUserId2, postId);
        assertTrue(likeController.hasUserLikedPost(user0(), likingUserId1, postId));
        likeController.unlikePost(user0(), likingUserId2, postId);
        assertFalse(likeController.hasUserLikedPost(user0(), likingUserId2, postId));

    }


}

