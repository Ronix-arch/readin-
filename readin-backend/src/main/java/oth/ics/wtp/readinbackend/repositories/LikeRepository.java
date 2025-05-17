package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.readinbackend.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByAppUserIdAndPostId(long appUserId, Long postId);
    int countByPostId(Long postId);


    void deleteByAppUserIdAndPostId(long appUserId, Long postId);
}
