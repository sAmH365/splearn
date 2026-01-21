package com.system.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.system.splearn.SplearnTestConfiguration;
import com.system.splearn.domain.member.DuplicateEmailException;
import com.system.splearn.domain.member.DuplicateProfileException;
import com.system.splearn.domain.member.Member;
import com.system.splearn.domain.member.MemberFixture;
import com.system.splearn.domain.member.MemberInfoUpdateRequest;
import com.system.splearn.domain.member.MemberRegisterRequest;
import com.system.splearn.domain.member.MemberStatus;
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
  void activate() {
    Member member = registerMember();

    member = memberRegister.activate(member.getId());
    entityManager.flush();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    assertThat(member.getDetail().getActivatedAt()).isNotNull();
  }

  private Member registerMember() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();
    return member;
  }

  private Member registerMember(String email) {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
    entityManager.flush();
    entityManager.clear();
    return member;
  }

  @Test
  void deactivate() {
    Member member = registerMember();

    member = memberRegister.activate(member.getId());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.deactivate(member.getId());

    assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
  }

  @Test
  void updateInfo(){
    Member member = registerMember();

    memberRegister.activate(member.getId());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "ttt11", "introduce my self"));

    assertThat(member.getDetail().getProfile().address()).isEqualTo("ttt11");
  }

  @Test
  void updateInfoFail(){
    Member member = registerMember();
    memberRegister.activate(member.getId());
    entityManager.flush();
    entityManager.clear();
    memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "ttt100", "introduce my self"));

    Member member2 = registerMember("user2@splearn.app");
    memberRegister.activate(member2.getId());
    entityManager.flush();
    entityManager.clear();

    // member2는 기존의 member와 같은 profile을 사용할 수 없다
    assertThatThrownBy(() ->  {
      memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("Peter", "ttt100", "introduce my self"));
    }).isInstanceOf(DuplicateProfileException.class);

    // 다른 프로필 주소로는 변경 가능
    memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("Peter", "ttt101", "introduce my self"));

    // 기존 프로필 주소를 바꾸는 것도 가능
    memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "ttt100", "introduce my self"));

    // 프로필 주소를 제거하는 것도 가능
    memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "", "introduce my self"));

    // 프로필 주소 중복은 허용하지 않음
    assertThatThrownBy(() ->  {
      memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "ttt101", "introduce my self"));
    }).isInstanceOf(DuplicateProfileException.class);
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
