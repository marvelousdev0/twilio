package com.vamshimaddur.spring.twilio.lookup;

import com.twilio.rest.lookups.v2.PhoneNumber;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  /**
   * Fetches reassigned number details for a given phone number and last verification date.
   *
   * @param phoneNumber Phone number for lookup
   * @param lastVerifiedDate Date of last verification (optional)
   * @return A map containing the reassigned number details
   */
  @GetMapping("/reassignedNumber")
  @Operation(
      summary = "Fetch reassigned number details",
      description =
          "Fetches reassigned number details for a given phone number and last verification date.",
      tags = {"Twilio Lookup"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  public Map<String, Object> fetchPhoneNumberDetails(
      @RequestParam @Schema(description = "Phone number for lookup", example = "1234567890")
          String phoneNumber,
      @RequestParam(required = false)
          @Schema(description = "Date of last verification", example = "yyyymmdd")
          String lastVerifiedDate) {
    logger.info(
        "Received request to fetch phone number details for phoneNumber: {} with lastVerifiedDate:"
            + " {}",
        phoneNumber,
        lastVerifiedDate);

    try {
      lookupConfig.init();
      PhoneNumber fetcher =
          PhoneNumber.fetcher(phoneNumber)
              .setFields("reassigned_number")
              .setLastVerifiedDate(lastVerifiedDate != null ? lastVerifiedDate : "")
              .fetch();
      Map<String, Object> response = fetcher.getReassignedNumber();
      logger.info("Successfully fetched phone number details for phoneNumber: {}", phoneNumber);
      return response;
    } catch (Exception ex) {
      logger.error("Error occurred while fetching details for phoneNumber: {}", phoneNumber, ex);
      throw ex;
    }
  }
}
