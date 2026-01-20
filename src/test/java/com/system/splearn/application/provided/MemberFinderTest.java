package com.system.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;

import com.system.splearn.SplearnTestConfiguration;
import com.system.splearn.domain.Member;
import com.system.splearn.domain.MemberFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(
    MemberFinder memberFinder,
    MemberRegister memberRegister,
    EntityManager entityManager
) {

  @Test
  void find(){
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    Member found = memberFinder.find(member.getId());

    assertThat(member.getId()).isEqualTo(found.getId());
  }
}
