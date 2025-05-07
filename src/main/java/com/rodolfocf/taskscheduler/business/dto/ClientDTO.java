package com.rodolfocf.taskscheduler.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {

    private String email;
    private String password;

}
