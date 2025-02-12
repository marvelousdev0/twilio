package com.vamshimaddur.spring.twilio.lookup.Rnd;

import jakarta.annotation.Nullable;

public class RndApiResponse {
  private final RndApiError error;
  private final String phoneNumber;
  private final String isNumberReassigned;

  public RndApiResponse(
      String phoneNumber, @Nullable String isNumberReassigned, @Nullable RndApiError error) {
    this.phoneNumber = phoneNumber;
    this.isNumberReassigned = isNumberReassigned;
    this.error = error;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getIsNumberReassigned() {
    return isNumberReassigned;
  }

  public RndApiError getError() {
    return error;
  }
}
