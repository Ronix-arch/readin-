package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity public class AppUser {
    @Id @GeneratedValue
    private Long id;
   @Column(unique=true) private String name;
    private  String hashedPassword;
    private Instant createdAt;

    public AppUser() {}

    public AppUser(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.createdAt = Instant.now();
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
