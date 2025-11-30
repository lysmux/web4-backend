package dev.lysmux.web4.db.hit;

import dev.lysmux.web4.db.user.UserEntity;
import dev.lysmux.web4.domain.model.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "hits")
public class HitEntity {
    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double r;

    @Column(nullable = false)
    private boolean hit;

    @Column(name = "execution_time", nullable = false)
    private long executionTime;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
