package com.ssafy.gumibom.domain.user.entity;

//public enum Role {
//    USER, MANAGER, ADMIN;
//}

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String value;
}


