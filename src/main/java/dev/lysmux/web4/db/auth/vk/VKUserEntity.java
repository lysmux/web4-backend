package dev.lysmux.web4.db.auth.vk;

import dev.lysmux.web4.db.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "vk_credentials")
public class VKUserEntity {
    @Id
    private UUID id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "vk_id", nullable = false)
    private long vkId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
