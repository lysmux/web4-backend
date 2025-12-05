package dev.lysmux.web4.auth.provider.vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lysmux.web4.auth.provider.vk.exception.VKAuthException;
import dev.lysmux.web4.auth.provider.vk.model.*;
import dev.lysmux.web4.auth.provider.vk.repository.VKAuthRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@ApplicationScoped
public class VKAuthProvider {
    private static final String EXCHANGE_URL = "https://id.vk.ru/oauth2/auth";
    private static final String USER_INFO_URL = "https://id.vk.ru/oauth2/user_info";

    @ConfigProperty(name = "auth.vk.client-id")
    String clientId;

    @ConfigProperty(name = "auth.vk.redirect-uri")
    String redirectUri;

    @Inject
    VKAuthRepository repository;

    @Inject
    UserService userService;

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public User register(VKCallback credentials) {
        ExchangeResponse exchanged = exchangeCode(credentials);
        VKUser vkUser = repository.getUser(exchanged.userId());
        System.out.println(exchanged.accessToken());
        if (vkUser == null) {
            UserInfoResponse userInfo = getUserInfo(exchanged.accessToken());

            User user = userService.createConfirmedUser("vk_" + userInfo.userId(), userInfo.email());
            vkUser = VKUser.builder()
                    .userId(user.getId())
                    .vkId(exchanged.userId())
                    .build();
            repository.addUser(vkUser);
            return user;
        }

        return userService.getById(vkUser.getUserId());
    }

    private ExchangeResponse exchangeCode(VKCallback credentials) {
        ExchangeRequest request = ExchangeRequest.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .code(credentials.code())
                .codeVerifier(credentials.challengeVerifier())
                .deviceId(credentials.deviceId())
                .state(UUID.randomUUID().toString())
                .build();

        try {
            return makeRequest(EXCHANGE_URL, request, ExchangeResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new VKAuthException("Could not exchange token");
        }
    }

    private UserInfoResponse getUserInfo(String accessToken) {
        UserInfoRequest request = UserInfoRequest.builder()
                .clientId(clientId)
                .accessToken(accessToken)
                .build();
        try {
            return makeRequest(USER_INFO_URL, request, UserInfoResponse.class, "/user");
        } catch (IOException | InterruptedException e) {
            throw new VKAuthException("Could not get user info");
        }
    }


    private <T> T makeRequest(String url, Object request, Class<T> responseType)
            throws IOException, InterruptedException {
        return makeRequest(url, request, responseType, null);
    }

    private <T> T makeRequest(
            String url,
            Object request,
            Class<T> responseType,
            String jsonPath
    ) throws IOException, InterruptedException {

        String requestBody = mapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        if (jsonPath != null && !jsonPath.isEmpty()) {
            return mapper.readerFor(responseType)
                    .at(jsonPath)
                    .readValue(responseBody);
        }
        return mapper.readValue(responseBody, responseType);
    }
}
