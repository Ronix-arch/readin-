package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oth.ics.wtp.readinbackend.entities.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByAppUser_IdAndPost_Id(long appUserId, Long postId);
    int countByPost_Id(Long postId);

    void deleteByAppUser_IdAndPost_Id(long appUserId, Long postId);
}
