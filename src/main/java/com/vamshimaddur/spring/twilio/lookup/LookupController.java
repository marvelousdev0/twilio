package com.vamshimaddur.spring.twilio.lookup;

import com.twilio.rest.lookups.v2.PhoneNumber;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/twilio/lookup")
public class LookupController {
  private static final Logger logger = LoggerFactory.getLogger(LookupController.class);

  private final LookupConfig lookupConfig;

  public LookupController(LookupConfig lookupConfig) {
    this.lookupConfig = lookupConfig;
  }

  @GetMapping("/reassignedNumber")
  public Map<String, Object> fetchPhoneNumberDetails(
      @RequestParam String phoneNumber, @RequestParam(required = false) String lastVerifiedDate) {
    logger.info(
        "Received request to fetch phone number details for phoneNumber: {} with lastVerifiedDate:"
            + " {}",
        phoneNumber,
        lastVerifiedDate);

    try {
      lookupConfig.init();
      PhoneNumber fetcher =
          PhoneNumber.fetcher(phoneNumber)
              .setLastVerifiedDate(lastVerifiedDate != null ? lastVerifiedDate : "")
              .fetch();
      Map<String, Object> response = fetcher.getReassignedNumber();
      logger.info("Successfully fetched phone number details for phoneNumber: {}", phoneNumber);
      return response;
    } catch (Exception ex) {
      logger.error("Error occurred while fetching details for phoneNumber: {}", phoneNumber, ex);
      throw ex; // Re-throwing the exception after logging
    }
  }
}
