package com.vamshimaddur.spring.twilio.lookup;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/twilio/lookup")
public class LookupController {

  private final LookupService service;

  public LookupController(LookupService service) {
    this.service = service;
  }

  @GetMapping("/reassignedNumber/{phoneNumber}")
  public Object fetchReassignedNumberDetails(
      @PathVariable String phoneNumber, @RequestParam String lastVerifiedDate) {
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      throw new LookupParameterException("Phone number is required");
    }
    if (lastVerifiedDate == null || lastVerifiedDate.isEmpty()) {
      throw new LookupParameterException("Last verified date is required");
    }
    return service.fetchReassignedNumberDetails(phoneNumber, lastVerifiedDate);
  }
}
