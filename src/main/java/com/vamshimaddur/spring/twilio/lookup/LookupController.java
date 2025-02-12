package com.vamshimaddur.spring.twilio.lookup;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/twilio/lookup")
public class LookupController {

  private final LookupService service;

  public LookupController(LookupService service) {
    this.service = service;
  }

  @GetMapping("/reassignedNumber")
  public Map<String, Object> fetchData() {
    return service.fetchReassignedNumberDetails();
  }
}
