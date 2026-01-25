package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String profilePictureUrl;
    @Column(unique = true)
    private String email;
    @Lob // Specifies that the database should store the string as a large object.
    private String bio;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Following> following = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Following> followers = new HashSet<>();


    public AppUser() {
    }

    public AppUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.createdAt = Instant.now();
        this.role = UserRole.USER; // Default role
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Following> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Following> following) {
        this.following = following;
    }

    public Set<Following> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Following> followers) {
        this.followers = followers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
