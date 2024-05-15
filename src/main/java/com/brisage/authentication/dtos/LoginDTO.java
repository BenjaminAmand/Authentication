package com.brisage.authentication.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @JsonProperty(value = "email")
    private String username;
    @JsonProperty(value = "pwd")
    private String password;
}
