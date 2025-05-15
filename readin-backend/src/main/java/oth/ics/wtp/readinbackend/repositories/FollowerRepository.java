package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.readinbackend.entities.Follower;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);
    List<Follower> findByFollowerId(long followerId);


}
