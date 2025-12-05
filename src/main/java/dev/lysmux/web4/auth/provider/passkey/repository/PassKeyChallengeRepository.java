package dev.lysmux.web4.auth.provider.passkey.repository;

import dev.lysmux.web4.auth.provider.passkey.model.PassKeyChallenge;

import java.util.UUID;

public interface PassKeyChallengeRepository {
    void addChallenge(PassKeyChallenge challenge);

    PassKeyChallenge getChallenge(UUID operationId);
}
