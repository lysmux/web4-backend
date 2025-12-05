package dev.lysmux.web4.db.auth.passkey;

import dev.lysmux.web4.db.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "passkey_credentials")
public class PassKeyCredentialEntity {
    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "credential_id", nullable = false, columnDefinition = "BYTEA")
    private byte[] credentialId;

    @Column(name = "public_key", nullable = false, columnDefinition = "BYTEA")
    private byte[] publicKey;

    @Column(name = "aagu_id", nullable = false, columnDefinition = "BYTEA")
    private byte[] aaguId;

    @Column(name = "sign_count", nullable = false)
    private long signCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
