package com.example.selfgrowth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {

    private String name;
    private String pwd;
}
