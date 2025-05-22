package oth.ics.wtp.readinbackend;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.readinbackend.controllers.AppUserController;
import oth.ics.wtp.readinbackend.controllers.FollowingController;
import oth.ics.wtp.readinbackend.controllers.LikeController;
import oth.ics.wtp.readinbackend.controllers.PostController;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreatePostDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppUserControllerTest extends ReadinControllerTestBase{
    @Autowired private AppUserController appUserController;
    @Autowired private PostController postController;
    @Autowired private LikeController likeController;
    @Autowired private FollowingController followingController;


    @Test public void testCreateList() {
     appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
     appUserController.createAppUser(new CreateAppUserDto("user2", "password2"));

     List<AppUserDto> appUsers = appUserController.getAppUsers(user0());
     assertTrue(appUsers.stream().anyMatch(u->u.name().equals("user1")));
     assertTrue(appUsers.stream().anyMatch(u->u.name().equals("user2")));

    }
    @Test public void testCreateLoginLogout() {
       // long appUserId1 = appUserController.createAppUser(new CreateAppUserDto("user1","password1")).id();
        //long postId1 = postController.createPost(user0(),appUserId1,new CreatePostDto( "post1")).id();
        appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
        appUserController.logIn(mockRequest("user1","password1"));
        assertDoesNotThrow(() -> appUserController.logIn(mockRequest("user1","password1")));
        assertDoesNotThrow(()->appUserController.logOut(mockRequest("user1","password1")));
    }

    @Test public void testCreateGetDelete() {
        appUserController.createAppUser(new CreateAppUserDto("user1", "password1"));
        appUserController.createAppUser(new CreateAppUserDto("user2", "password2"));
        AppUserDto appUser = appUserController.getAppUser(user0(),"user1");
        assertEquals("user1",appUser.name());
        appUserController.deleteAppUser(user0(),"user1");
        assertThrows(ResponseStatusException.class, ()->appUserController.getAppUser(user0(),"user1"));


    }

}
