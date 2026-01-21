package com.system.splearn.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProfileTest {

  @Test
  void profile(){
   new Profile("tootototot");
    new Profile("to11");
    new Profile("12345");
    new Profile("");
  }

  @Test
  void profileFail(){
    assertThatThrownBy(() -> new Profile("tolongtolongtolongtolongtolongtolong")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Profile("가나다")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void url(){
     var profile = new Profile("propro");

     assertThat(profile.url()).isEqualTo("@propro");
  }
}
