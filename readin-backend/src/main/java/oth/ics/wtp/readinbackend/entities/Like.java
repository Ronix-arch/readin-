package oth.ics.wtp.readinbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
//@Table(name = "like",uniqueConstraints =
//@UniqueConstraint(ColumnNames = {})) coming back
public class Like {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne @OnDelete(action = CASCADE)  private AppUser user;
    @ManyToOne @OnDelete(action = CASCADE) private Post post;

    private Instant createdAt;

}
