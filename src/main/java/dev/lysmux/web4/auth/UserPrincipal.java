package dev.lysmux.web4.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class UserPrincipal implements Principal {
    private UUID id;

    private String username;

    private String email;

    @Override
    public String getName() {
        return id.toString();
    }
}
