package com.system.splearn.adapter.webapi;

import static com.system.splearn.AssertThatUtils.equalsTo;
import static com.system.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.system.splearn.application.member.provided.MemberRegister;
import com.system.splearn.application.member.required.MemberRepository;
import com.system.splearn.domain.member.Member;
import com.system.splearn.domain.member.MemberFixture;
import com.system.splearn.domain.member.MemberRegisterRequest;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberApiTest {
  @Autowired
  MockMvcTester mvcTester;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MemberRegister memberRegister;

  @Test
  void register() throws JsonProcessingException, UnsupportedEncodingException {
    MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mvcTester.post().uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.memberId", notNull())
        .hasPathSatisfying("$.email", equalsTo(request));

    MemberRegisterResponse response = objectMapper.readValue(
        result.getResponse().getContentAsString(), MemberRegisterResponse.class);
    Member member = memberRepository.findById(response.memberId()).orElseThrow();

    assertThat(member.getEmail().address()).isEqualTo(request.email());
    assertThat(member.getNickname()).isEqualTo(request.nickname());
  }

  @Test
  void duplicateEmail() throws JsonProcessingException {
    memberRegister.register(MemberFixture.createMemberRegisterRequest());

    MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mvcTester.post().uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson).exchange();

    assertThat(result)
        .apply(MockMvcResultHandlers.print())
        .hasStatus(HttpStatus.CONFLICT);
  }
}
