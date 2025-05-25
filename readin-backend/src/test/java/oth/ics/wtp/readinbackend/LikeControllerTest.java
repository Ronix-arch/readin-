package oth.ics.wtp.readinbackend;

import org.springframework.beans.factory.annotation.Autowired;
import oth.ics.wtp.readinbackend.controllers.LikeController;
import oth.ics.wtp.readinbackend.controllers.PostController;

public class LikeControllerTest extends ReadinControllerTestBase{
    @Autowired
    private PostController postController;
    @Autowired private LikeController likeController;
}
