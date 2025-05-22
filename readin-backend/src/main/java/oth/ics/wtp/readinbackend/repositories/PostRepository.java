package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oth.ics.wtp.readinbackend.entities.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAppUser_IdOrderByCreatedAtDesc(long AppUserId);
    @Query("SELECT p FROM Post p JOIN Following f ON p.appUser.id = f.followee.id " +
            "WHERE f.follower.id = :followerId ORDER BY p.createdAt DESC")
    List<Post> findTimelinePosts(@Param("followerId") long followerId);


    Optional<Post> findByAppUser_IdAndId(long appUserId, long id);

    boolean existsByAppUser_IdAndId( long appUserId,Long id);
// not yet used
//    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p.id ORDER BY p.createdAt DESC")
//    List<Post> findAllPostsWithLikeCount();


}
