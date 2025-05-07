package com.rodolfocf.taskscheduler.infrastructure.client;

import com.rodolfocf.taskscheduler.business.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client", url = "${client.url}")
public interface ClientClient {

    @GetMapping("/client")
    ClientDTO searchClientByEmail(@RequestParam("email") String email, @RequestHeader("Authorization") String token);

}
