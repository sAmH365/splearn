package com.system.splearn.adapter.integration;

import com.system.splearn.application.required.EmailSender;
import com.system.splearn.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {

  @Override
  public void send(Email email, String subject, String content) {
    System.out.println("DummyEmailSender: " + email);
  }
}
