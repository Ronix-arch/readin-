package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Table(name = "follower", uniqueConstraints = {
        @UniqueConstraint(name = "unique_follower_followee", columnNames = {"follower", "followee"})
})

@Entity public class Follower {
    @Id @GeneratedValue private Long id;
    @ManyToOne @JoinColumn(name = "follower",nullable = false) private AppUser follower;
    @ManyToOne @JoinColumn(name = "followee",nullable = false) private AppUser followee;
    private Instant createdAt;

    public Follower (){}
    public Follower(AppUser follower, AppUser followee) {
        this.follower = follower;
        this.followee = followee;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getFollower() {
        return follower;
    }

    public void setFollower(AppUser follower) {
        this.follower = follower;
    }

    public AppUser getFollowee() {
        return followee;
    }

    public void setFollowee(AppUser followee) {
        this.followee = followee;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
