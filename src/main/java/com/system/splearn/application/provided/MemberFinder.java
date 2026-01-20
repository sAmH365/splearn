package com.system.splearn.application.provided;

import com.system.splearn.domain.Member;

public interface MemberFinder {

  /**
   * 회원을 조회한다.
   * @return
   */
  Member find(Long memberId);

}
