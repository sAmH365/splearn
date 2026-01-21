package com.system.splearn.application.member;

import com.system.splearn.application.member.provided.MemberFinder;
import com.system.splearn.application.member.provided.MemberRegister;
import com.system.splearn.application.member.required.EmailSender;
import com.system.splearn.application.member.required.MemberRepository;
import com.system.splearn.domain.member.DuplicateEmailException;
import com.system.splearn.domain.shared.Email;
import com.system.splearn.domain.member.Member;
import com.system.splearn.domain.member.MemberRegisterRequest;
import com.system.splearn.domain.member.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
  private final MemberFinder memberFinder;
  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Member register(MemberRegisterRequest registerRequest) {
    checkDuplicateEmail(registerRequest);

    Member member = Member.register(registerRequest, passwordEncoder);

    memberRepository.save(member);

    sendWelcomeEmail(member);

    return member;
  }

  @Override
  public Member activate(Long memberId) {
    Member member = memberFinder.find(memberId);

    member.activate();

    return memberRepository.save(member);
  }

  private void sendWelcomeEmail(Member member) {
    emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
  }

  private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
    if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
      throw new DuplicateEmailException("이미 사용중인 이메일입니다:" + registerRequest.email());
    }
  }
}
