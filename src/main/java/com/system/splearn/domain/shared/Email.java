package com.system.splearn.domain.shared;

import java.util.regex.Pattern;

public record Email(
    String address
) {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$");

  public Email {
    if (address == null || !EMAIL_PATTERN.matcher(address).matches()) {
      throw new IllegalArgumentException("Invalid email format: " + address);
    }
  }
}
