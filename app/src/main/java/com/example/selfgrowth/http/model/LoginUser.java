package com.example.selfgrowth.http.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {

    private String email;
    private String password;
}
