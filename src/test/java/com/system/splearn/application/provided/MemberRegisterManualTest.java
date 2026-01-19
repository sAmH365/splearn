package com.system.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

import com.system.splearn.application.MemberService;
import com.system.splearn.application.required.EmailSender;
import com.system.splearn.application.required.MemberRepository;
import com.system.splearn.domain.Email;
import com.system.splearn.domain.Member;
import com.system.splearn.domain.MemberFixture;
import com.system.splearn.domain.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class MemberRegisterManualTest {

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

  @Test
  void registerTestMockito(){
    EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
    );

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
  }


  static class MemberRepositoryStub implements MemberRepository {

    @Override
    public Member save(Member member) {
      ReflectionTestUtils.setField(member, "id", 1L);
      return null;
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
      return Optional.empty();
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
