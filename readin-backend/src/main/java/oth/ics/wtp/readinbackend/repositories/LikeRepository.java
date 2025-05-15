package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.readinbackend.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndPostId(long userId, Long postId);
    int countByPostId(Long postId);


}
