package com.system.splearn.adapter.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.system.splearn.domain.Email;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class DummyEmailSenderTest {

  @Test
  @StdIo
  void dummyEmailSender(StdOut out){
    DummyEmailSender dummyEmailSender = new DummyEmailSender();
    dummyEmailSender.send(new Email("sam@naver.com"), "subject", "body");

    assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender: Email[address=sam@naver.com]");
  }
}
