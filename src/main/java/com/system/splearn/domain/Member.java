package com.system.splearn.domain;

import static org.springframework.util.Assert.*;

import java.util.Objects;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {

  private String email;

  private String nickname;

  private String passwordHash;

  private MemberStatus status;


  public Member(String email, String nickname, String passwordHash) {
    this.email = Objects.requireNonNull(email);
    this.nickname = Objects.requireNonNull(nickname);
    this.passwordHash = Objects.requireNonNull(passwordHash);

    this.status = MemberStatus.PENDING;
  }

  public void activate() {
//    if (status != MemberStatus.PENDING) throw new IllegalStateException("PENDING 상태가 아닙니다.");
    state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTIVE;
  }

  public void deactivate() {
    state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
  }
}
