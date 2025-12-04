package dev.lysmux.web4.auth.provider.vk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lysmux.web4.auth.provider.vk.exception.VKAuthException;
import dev.lysmux.web4.auth.provider.vk.model.*;
import dev.lysmux.web4.auth.provider.vk.repository.VKAuthRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ApplicationScoped
public class VKAuthProvider {
    private static final String EXCHANGE_URL = "https://id.vk.ru/oauth2/auth";
    private static final String USER_INFO_URL = "https://id.vk.ru/oauth2/user_info";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    VKAuthRepository repository;

    @Inject
    UserService userService;

    @Transactional
    public User register(VKCredentials credentials) {
        ExchangeResponse exchanged = exchangeCode(credentials);
        VKUser vkUser = repository.getUser(exchanged.user_id());

        if (vkUser == null) {
            UserInfoResponse userInfo = getUserInfo(exchanged.access_token());

            User user = userService.createConfirmedUser("vk_" + exchanged.user_id(), userInfo.email());
            vkUser = VKUser.builder()
                    .userId(user.getId())
                    .vkId(exchanged.user_id())
                    .build();
            repository.addUser(vkUser);
            return user;
        }

        return userService.getById(vkUser.getUserId());
    }

    private ExchangeResponse exchangeCode(VKCredentials credentials) {
        ExchangeRequest request = ExchangeRequest.builder()
                .code(credentials.code())
                .code_verifier(credentials.challengeVerifier())
                .device_id(credentials.deviceId())
                .state(UUID.randomUUID().toString())
                .build();

        return makeRequest(request);
    }

    private UserInfoResponse getUserInfo(String accessToken) {
        UserInfoRequest request = UserInfoRequest.builder()
                .access_token(accessToken)
                .build();
        return makeRequest2(request);
    }

    private ExchangeResponse makeRequest(ExchangeRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(EXCHANGE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), ExchangeResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new VKAuthException("Could not exchange token");
        }
    }

    private UserInfoResponse makeRequest2(UserInfoRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(USER_INFO_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "client_id=" + URLEncoder.encode(request.getClient_id(), StandardCharsets.UTF_8) +
                            "&access_token=" + URLEncoder.encode(request.getAccess_token(), StandardCharsets.UTF_8)
                    ))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode userNode = root.get("user");

            return mapper.treeToValue(userNode, UserInfoResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new VKAuthException("Could not get user info");
        }
    }
}
