package com.system.splearn.application.member.required;

import com.system.splearn.domain.member.Profile;
import com.system.splearn.domain.shared.Email;
import com.system.splearn.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {
  Member save(Member member);

  Optional<Member> findByEmail(Email email);

  Optional<Member> findById(Long memberId);

  @Query("select m from Member m where m.detail.profile = :profile")
  Optional<Member> findByProfile(Profile profile);
}
