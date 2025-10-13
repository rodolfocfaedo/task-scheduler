package io.github.rodolfocf.taskscheduler.infrastructure.client;

import io.github.rodolfocf.taskscheduler.infrastructure.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "${user.url}")
public interface UserClient {

    @GetMapping
    UserResponseDTO getUserByEmail(@RequestParam("email") String email,
                                   @RequestHeader("Authorization") String token);

}
