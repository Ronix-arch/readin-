package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import oth.ics.wtp.readinbackend.entities.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdOrderByCreatedAtDesc(long userId);
    @Query("SELECT p FROM Post p JOIN Follower f ON p.user.id = f.followee.id " +
            "WHERE f.follower.id = :followerId ORDER BY p.createdAt DESC")
    List<Post> findTimelinePosts(@Param("followerId") long followerId);



}
