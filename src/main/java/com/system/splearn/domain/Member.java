package com.system.splearn.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

  @NaturalId
  private Email email;

  private String nickname;

  private String passwordHash;

  private MemberStatus status;

  private MemberDetail detail;

  public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(requireNonNull(registerRequest.email()));
    member.nickname = requireNonNull(registerRequest.nickname());
    member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));

    member.status = MemberStatus.PENDING;

    return member;
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

  public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, this.passwordHash);
  }

  public void changeNickname(String nickname) {
    this.nickname = requireNonNull(nickname);
  }

  public void changePassword(String password, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(requireNonNull(password));
  }

  public boolean isActive() {
    return this.status == MemberStatus.ACTIVE;
  }
}
