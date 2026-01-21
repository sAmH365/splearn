package com.system.splearn.application.member.provided;

import com.system.splearn.domain.member.Member;

public interface MemberFinder {

  /**
   * 회원을 조회한다.
   * @return
   */
  Member find(Long memberId);

}
