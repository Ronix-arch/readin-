package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "like",uniqueConstraints = {
@UniqueConstraint( name = "uniquepostuser",columnNames = {"user","post"})})
public class Like {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne @OnDelete(action = CASCADE)  private AppUser user;
    @ManyToOne @OnDelete(action = CASCADE) private Post post;
    private Instant createdAt;

    public Like() {}
    public Like(AppUser user, Post post) {
        this.user = user;
        this.post = post;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user_id) {
        this.user = user_id;
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
