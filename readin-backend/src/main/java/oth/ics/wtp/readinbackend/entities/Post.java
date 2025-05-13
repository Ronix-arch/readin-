package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity public class Post {
    @Id @GeneratedValue
    private Long id;
    private String content;
    private Instant createdAt;
    @ManyToOne @OnDelete (action = CASCADE) private AppUser user;

    public Post() {
    }

    public Post(String content, AppUser user) {
        this.content = content;
        this.user = user;
        this.createdAt = Instant.now();
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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser author) {
        this.user = author;
    }
}
