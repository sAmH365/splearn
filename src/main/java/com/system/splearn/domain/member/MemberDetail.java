package com.system.splearn.domain.member;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import com.system.splearn.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

  @Embedded
  private Profile profile;

  @Column(columnDefinition = "TEXT")
  private String introduction;

  @Column(nullable = false)
  private LocalDateTime registeredAt;

  private LocalDateTime activatedAt;

  private LocalDateTime deactivatedAt;

  static MemberDetail create() {
    MemberDetail memberDetail = new MemberDetail();
    memberDetail.registeredAt = LocalDateTime.now();
    return memberDetail;
  }

  void activate() {
    Assert.isTrue(activatedAt == null, "이미 activatedAt은 설정되었습니다.");

    this.activatedAt = LocalDateTime.now();
  }

  void deactivate() {
    Assert.isTrue(deactivatedAt == null, "이미 deactivatedAt은 설정되었습니다.");

    this.deactivatedAt = LocalDateTime.now();
  }

  public void updateInfo(MemberInfoUpdateRequest updateRequest) {
    this.profile = new Profile(updateRequest.profileAddress());
    this.introduction = Objects.requireNonNull(updateRequest.introduction());
  }
}
