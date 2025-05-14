package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity public class AppUser {
    @Id @GeneratedValue
    private Long id;
   @Column(unique=true) private String name;
    private  String hashedPassword;
    private Instant createdAt;
    @OneToMany(mappedBy = "followee")
    private List<Follower> followers; // Users who follow this user

    @OneToMany(mappedBy = "follower")
    private List<Follower> following; // Users this user follows

    public AppUser() {}

    public AppUser(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.createdAt = Instant.now();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Follower> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follower> following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
