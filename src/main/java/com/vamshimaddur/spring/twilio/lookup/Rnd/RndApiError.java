package com.vamshimaddur.spring.twilio.lookup.Rnd;

public class RndApiError {
  private final int code;
  private final String description;

  public RndApiError(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
