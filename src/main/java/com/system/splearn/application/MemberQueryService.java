package com.system.splearn.application;

import com.system.splearn.application.provided.MemberFinder;
import com.system.splearn.application.required.MemberRepository;
import com.system.splearn.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

  private final MemberRepository memberRepository;

  @Override
  public Member find(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
  }
}
