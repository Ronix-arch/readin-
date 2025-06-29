package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(name = "uniquepostuser", columnNames = {"app_user_id", "post_id"})})
public class Like {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    @OnDelete(action = CASCADE)
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @OnDelete(action = CASCADE)
    private Post post;
    private Instant createdAt;

    public Like() {
    }

    public Like(AppUser appUser, Post post) {
        this.appUser = appUser;
        this.post = post;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser user_id) {
        this.appUser = user_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post_id) {
        this.post = post_id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
