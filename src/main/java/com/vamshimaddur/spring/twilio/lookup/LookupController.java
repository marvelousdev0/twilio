package com.vamshimaddur.spring.twilio.lookup;

import com.vamshimaddur.spring.twilio.lookup.Rnd.RndApiResponse;
import com.vamshimaddur.spring.twilio.lookup.Rnd.RndService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/twilio/lookup")
public class LookupController {

  private final RndService rndService;

  public LookupController(RndService rndService) {
    this.rndService = rndService;
  }

  /**
   * Endpoint to fetch reassigned number details for a given phone number.
   *
   * <p>Uses the Twilio API to retrieve information about whether the specified phone number has
   * been reassigned as of the provided last verified date.
   *
   * @param phoneNumber the phone number to lookup
   * @param lastVerifiedDate the date to check if the number was reassigned
   * @return {@link RndApiResponse} containing the reassigned number details
   */
  @GetMapping("/reassignedNumber/{phoneNumber}")
  public RndApiResponse fetchReassignedNumberDetails(
      @PathVariable String phoneNumber, @RequestParam String lastVerifiedDate) {
    return rndService.callExternalApi(phoneNumber, lastVerifiedDate);
  }
}
