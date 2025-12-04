package dev.lysmux.web4.auth.provider.vk.repository;


import dev.lysmux.web4.auth.provider.vk.model.VKUser;

public interface VKAuthRepository {
    void addUser(VKUser user);

    VKUser getUser(long vkId);
}
