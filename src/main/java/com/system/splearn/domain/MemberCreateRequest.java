package com.system.splearn.domain;

public record MemberCreateRequest(
    String email, String nickname, String password
) {

}
