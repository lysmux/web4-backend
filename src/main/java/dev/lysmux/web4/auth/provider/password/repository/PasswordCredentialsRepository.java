package dev.lysmux.web4.auth.provider.password.repository;


import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;

import java.util.UUID;

public interface PasswordCredentialsRepository {
    void addCredential(PasswordCredentials credentials);

    PasswordCredentials getByUserId(UUID userId);
}
