package com.example.selfgrowth.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {

    private String email;
    private String password;
    private List<String> applications;
}
