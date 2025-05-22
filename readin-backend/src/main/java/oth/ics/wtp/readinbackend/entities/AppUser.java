package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Entity public class AppUser {
    @Id @GeneratedValue
    private Long id;
   @Column(unique=true) private String name;
    private  String hashedPassword;
    private Instant createdAt;
    @OneToMany(mappedBy = "followee")
    private List<Following> followers; // Users who follow this user

    @OneToMany(mappedBy = "follower")
    private List<Following> followings; // Users this user follows
    @OneToMany (fetch = EAGER) private List<Post> appUserPosts;

    public AppUser() {}

    public AppUser(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.createdAt = Instant.now();
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        appUserPosts = new ArrayList<>();
    }

    public List<Following> getFollowers() {
        return followers ;
    }

    public void setFollowers(List<Following> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowing(List<Following> followings) {
        this.followings = followings;
    }

    public String getName() {
        return name;
    }

    public List<Post> getAppUserPosts() {
        return appUserPosts;
    }

    public void setAppUserPosts(List<Post> appUserPosts) {
        this.appUserPosts = appUserPosts;
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
