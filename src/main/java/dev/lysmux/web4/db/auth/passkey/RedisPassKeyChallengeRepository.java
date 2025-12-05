package dev.lysmux.web4.db.auth.passkey;

import dev.lysmux.web4.auth.provider.passkey.model.PassKeyChallenge;
import dev.lysmux.web4.auth.provider.passkey.repository.PassKeyChallengeRepository;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class RedisPassKeyChallengeRepository implements PassKeyChallengeRepository {
    private final ValueCommands<String, PassKeyChallenge> commands;

    private final static SetArgs SET_ARGS = new SetArgs().ex(Duration.ofMinutes(10));

    public RedisPassKeyChallengeRepository(RedisDataSource ds) {
        commands = ds.value(PassKeyChallenge.class);
    }

    @Override
    public void addChallenge(PassKeyChallenge challenge) {
        commands.set(challenge.getOperationId().toString(), challenge, SET_ARGS);
    }

    @Override
    public PassKeyChallenge getChallenge(UUID operationId) {
        return commands.get(operationId.toString());
    }
}
