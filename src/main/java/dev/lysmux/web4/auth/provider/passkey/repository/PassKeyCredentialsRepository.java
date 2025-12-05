package dev.lysmux.web4.auth.provider.passkey.repository;


import dev.lysmux.web4.auth.provider.passkey.model.PassKeyCredential;

import java.util.List;
import java.util.UUID;

public interface PassKeyCredentialsRepository {
    void addCredential(PassKeyCredential credential);

    PassKeyCredential getCredential(byte[] id);

    List<PassKeyCredential> getUserCredentials(UUID userId);
}
