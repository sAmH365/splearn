package com.system.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.system.splearn.SplearnTestConfiguration;
import com.system.splearn.domain.DuplicateEmailException;
import com.system.splearn.domain.Member;
import com.system.splearn.domain.MemberFixture;
import com.system.splearn.domain.MemberRegisterRequest;
import com.system.splearn.domain.MemberStatus;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
    MemberRegister memberRegister,
    EntityManager entityManager
) {
  @Test
  void register() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    System.out.println(member);

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
  void activate(){
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.activate(member.getId());
    entityManager.flush();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
  }


  @Test
  void memberRegisterRequestFail() {
    checkValidation(new MemberRegisterRequest("sam1@naver.com", "sam", "longsecret"));
    checkValidation(new MemberRegisterRequest("sam1@naver.com", "Charlie__________________", "longsecret"));
    checkValidation(new MemberRegisterRequest("sam1naver.com", "Charlie__________________", "longsecret"));
  }

  private void checkValidation(MemberRegisterRequest invalid) {
    assertThatThrownBy(() -> memberRegister.register(invalid))
    .isInstanceOf(ConstraintViolationException.class);
  }
}
