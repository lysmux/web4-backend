package dev.lysmux.web4.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lysmux.web4.auth.provider.vk.model.ExchangeRequest;
import dev.lysmux.web4.auth.provider.vk.model.UserInfoResponse;

import java.util.UUID;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        String response = "{\"user_id\":\"151214576\",\"first_name\":\"\\u041a\\u0438\\u0440\\u0438\\u043b\\u043b\",\"last_name\":\"\\u0420\\u0430\\u0437\\u044b\\u0433\\u0440\\u0430\\u0435\\u0432\",\"avatar\":\"https:\\/\\/sun9-81.vkuserphoto.ru\\/impg\\/DW4IDqvukChyc-WPXmzIot46En40R00idiUAXw\\/l5w5aIHioYc.jpg?quality=96&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360&sign=10ad7d7953daabb7b0e707fdfb7ebefd&cs=50x50\",\"email\":\"kllraz@vk.com\",\"sex\":2,\"verified\":false,\"birthday\":\"08.06.2006\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

//        UserInfoResponse resp = objectMapper.readerFor(UserInfoResponse.class)
//                .at("/user")
//                .readValue(response);

        System.out.println(objectMapper.readValue(response, UserInfoResponse.class));
    }
}
