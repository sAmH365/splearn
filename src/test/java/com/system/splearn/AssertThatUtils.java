package com.system.splearn;

import com.system.splearn.domain.member.MemberRegisterRequest;
import java.util.function.Consumer;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.springframework.test.json.JsonPathValueAssert;

public class AssertThatUtils {

  public static Consumer<AssertProvider<JsonPathValueAssert>> notNull() {
    return value -> Assertions.assertThat(value).isNotNull();
  }

  public static Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(
      MemberRegisterRequest request) {
    return value -> Assertions.assertThat(value).isEqualTo(request.email());
  }
}
