package oth.ics.wtp.readinbackend;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.readinbackend.controllers.AppUserController;
import oth.ics.wtp.readinbackend.controllers.FollowingController;
import oth.ics.wtp.readinbackend.controllers.PostController;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.dtos.PostDto;
import oth.ics.wtp.readinbackend.dtos.UpdatePostDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostControllerTest extends ReadinControllerTestBase {
    @Autowired
    private PostController postController;
    @Autowired
    private AppUserController appUserController;
    @Autowired
    private FollowingController followingController;


    private long appUserId;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        appUserId = appUserController.createAppUser(new CreateAppUserDto("user1", "password1")).id();


    }

    @Test
    public void testPostsListEmpty() {
        assertTrue(postController.getOwnPosts(user0(), appUserId, 0, 20).isEmpty());
    }

    @Test
    public void testCreatePostAndListPosts() {
        postController.createPost(user0(), appUserId, "post1OfUser1", null);
        postController.createPost(user0(), appUserId, "post2OfUser1", null);
        postController.createPost(user0(), appUserId, "post3OfUser1", null);
        List<PostDto> posts = postController.getOwnPosts(user0(), appUserId, 0, 20).toList();
        assertEquals(3, posts.size());
        assertTrue(posts.stream().anyMatch(p -> p.content().equals("post1OfUser1")));
        assertTrue(posts.stream().anyMatch(p -> p.content().equals("post2OfUser1")));
    }

    @Test
    public void testUpdatePost() {
        long postId = postController.createPost(user0(), appUserId, "post1OfUser1", null).id();
        PostDto postDto = postController.updatePost(user0(), appUserId, postId, new UpdatePostDto("updatedPost1OfUser1"));
        assertEquals("updatedPost1OfUser1", postDto.content());
        assertThrows(ResponseStatusException.class, () -> postController.updatePost(user0(), 34, postId, new UpdatePostDto("updatedPost2OfUser1")));
        assertThrows(ResponseStatusException.class, () -> postController.updatePost(user0(), appUserId, 34, new UpdatePostDto("updatedPost2OfUser1")));

    }

    @Test
    public void testDeletePost() {
        long postId = postController.createPost(user0(), appUserId, "post1OfUser1", null).id();
        postController.deletePost(user0(), appUserId, postId);
        assertThrows(ResponseStatusException.class, () -> postController.deletePost(user0(), appUserId, postId));

    }

    @Test
    public void testGetTimeLinePosts() {
        long followee1Id = appUserController.createAppUser(new CreateAppUserDto("followee1", "password2")).id();
        long followee2Id = appUserController.createAppUser(new CreateAppUserDto("followee2", "password3")).id();
        followingController.followUser(user0(), appUserId, followee1Id);
        followingController.followUser(user0(), appUserId, followee2Id);
        postController.createPost(user0(), followee1Id, "post1OfFollowee1", null);
        postController.createPost(user0(), followee2Id, "post1OfFollowee2", null);

        List<PostDto> timelinePosts = postController.getTimeLinePosts(user0(), appUserId, 0, 20).toList();
        assertFalse(timelinePosts.isEmpty());
        //assertEquals(2, timelinePosts.size());
        //assertTrue(timelinePosts.stream().anyMatch(p->p.content().equals("post1OfFollowee1")));
        //assertTrue(timelinePosts.stream().anyMatch(p->p.content().equals("post1OfFollowee2")));
        assertEquals(2, timelinePosts.size());
        assertTrue(timelinePosts.stream().anyMatch(p -> p.content().equals("post1OfFollowee1")));
        assertTrue(timelinePosts.stream().anyMatch(p -> p.content().equals("post1OfFollowee2")));


    }

}
