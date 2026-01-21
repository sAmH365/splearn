package com.system.splearn.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void equality() {
    var email1 = new Email("sam1@naver.com");
    var email2 = new Email("sam1@naver.com");

    assertThat(email1).isEqualTo(email2);
  }
}
