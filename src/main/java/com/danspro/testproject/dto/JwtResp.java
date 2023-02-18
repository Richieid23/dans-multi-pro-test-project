package com.danspro.testproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JwtResp {
    private String UserId;
    private String username;
}
