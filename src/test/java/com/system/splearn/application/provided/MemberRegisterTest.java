package com.system.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.system.splearn.application.MemberService;
import com.system.splearn.application.required.EmailSender;
import com.system.splearn.application.required.MemberRepository;
import com.system.splearn.domain.Email;
import com.system.splearn.domain.Member;
import com.system.splearn.domain.MemberFixture;
import com.system.splearn.domain.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MemberRegisterTest {

  @Test
  void registerTestStub(){
     MemberRegister register = new MemberService(
         new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
     );

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void registerTestMock(){
    EmailSenderMock emailSenderMock = new EmailSenderMock();
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
    );

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    assertThat(emailSenderMock.getTos()).hasSize(1);
    assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
  }


  static class MemberRepositoryStub implements MemberRepository {

    @Override
    public Member save(Member member) {
      ReflectionTestUtils.setField(member, "id", 1L);
      return null;
    }
  }

  static class EmailSenderStub implements EmailSender {

    @Override
    public void send(Email email, String subject, String content) {

    }
  }

  static class EmailSenderMock implements EmailSender {
    List<Email> tos = new ArrayList<>();
    @Override
    public void send(Email email, String subject, String content) {
      tos.add(email);
    }

    public List<Email> getTos() {
      return tos;
    }
  }
}
