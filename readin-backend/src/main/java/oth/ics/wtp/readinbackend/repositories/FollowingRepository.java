package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oth.ics.wtp.readinbackend.entities.Following;

import java.util.List;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);

    List<Following> findByFollowerId(long followerId);

    void deleteByFollowerIdAndFolloweeId(long followerId, long followeeId);

    List<Following> findByFolloweeId(long followeeId);
}
