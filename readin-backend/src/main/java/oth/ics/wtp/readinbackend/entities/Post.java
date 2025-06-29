package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "app_user_id")  // Ensure the correct column is mapp

    @OnDelete(action = CASCADE)
    private AppUser appUser;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;


    public Post() {
    }

    public Post(String content, AppUser appUser) {
        this.content = content;
        this.appUser = appUser;
        this.createdAt = Instant.now();
        this.likes = new ArrayList<>();
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser author) {
        this.appUser = author;
    }
}
