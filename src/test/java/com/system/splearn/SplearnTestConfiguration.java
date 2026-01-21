package com.system.splearn;

import com.system.splearn.application.member.required.EmailSender;
import com.system.splearn.domain.member.MemberFixture;
import com.system.splearn.domain.member.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {

  @Bean
  public EmailSender emailSender() {
    return (email, subject, content) -> System.out.println("Sending email: " + email);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return MemberFixture.createPasswordEncoder();
  }
}
