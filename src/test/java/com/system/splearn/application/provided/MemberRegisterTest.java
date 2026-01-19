package com.system.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.system.splearn.SplearnTestConfiguration;
import com.system.splearn.domain.DuplicateEmailException;
import com.system.splearn.domain.Member;
import com.system.splearn.domain.MemberFixture;
import com.system.splearn.domain.MemberRegisterRequest;
import com.system.splearn.domain.MemberStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(
    MemberRegister memberRegister
) {
  @Test
  void register() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void duplicateEmailFail() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest())).isInstanceOf(
        DuplicateEmailException.class);

  }

  @Test
  void memberRegisterRequestFail() {
    extracted(new MemberRegisterRequest("sam1@naver.com", "sam", "longsecret"));
    extracted(new MemberRegisterRequest("sam1@naver.com", "Charlie__________________", "longsecret"));
    extracted(new MemberRegisterRequest("sam1naver.com", "Charlie__________________", "longsecret"));
  }

  private void extracted(MemberRegisterRequest invalid) {
    assertThatThrownBy(() -> memberRegister.register(invalid))
    .isInstanceOf(ConstraintViolationException.class);
  }
}
