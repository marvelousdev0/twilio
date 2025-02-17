package com.vamshimaddur.spring.twilio.lookup.Rnd;

import com.vamshimaddur.spring.twilio.helpers.ParameterException;
import com.vamshimaddur.spring.twilio.lookup.LookupConfig;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RndService {
  private static final Logger logger = LoggerFactory.getLogger(RndService.class);
  // Error code mapping
  private static final Map<Integer, String> ERROR_MAP = new HashMap<>();

  // Initialize error code mapping
  static {
    ERROR_MAP.put(60004, "Invalid Configuration");
    ERROR_MAP.put(60607, "No provider found to satisfy the Lookup request");
    ERROR_MAP.put(
        60617,
        "Missing at least one valid additional argument to execute the Lookup you requested");
    ERROR_MAP.put(60618, "An invalid value was passed for query parameters");
  }

  private final WebClient webClient;

  public RndService(WebClient.Builder webClientBuilder, LookupConfig lookupConfig) {
    String basicAuth = lookupConfig.init();

    this.webClient =
        webClientBuilder
            .baseUrl("https://lookups.twilio.com/v2")
            .defaultHeader("Authorization", "Basic " + basicAuth)
            .build();
  }

  /**
   * Fetches the reassigned number details from Twilio API.
   *
   * <p>Requires a valid phone number and last verified date.
   *
   * @param phoneNumber the phone number to lookup
   * @param lastVerifiedDate the date to check if the number was reassigned
   * @return {@link RndApiResponse} containing the reassigned number details
   * @throws ParameterException if the phone number or last verified date is empty
   * @throws RuntimeException if there is an error calling the Twilio API
   */
  public RndApiResponse callExternalApi(String phoneNumber, String lastVerifiedDate) {
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      throw new ParameterException("Phone number is required");
    }
    if (lastVerifiedDate == null || lastVerifiedDate.isEmpty()) {
      throw new ParameterException("Last verified date is required");
    }

    logger.info(
        "Fetching reassigned number details for phone number: {} and lastVerifiedDate: {}",
        phoneNumber,
        lastVerifiedDate);

    try {
      Object reassignedResponse =
          webClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/PhoneNumbers/{phoneNumber}") // Path parameter
                          .queryParam("Fields", "reassigned_number") // Query parameter
                          .queryParam("LastVerifiedDate", lastVerifiedDate) // Query parameter
                          .build(phoneNumber)) // Replace {id} with resourceId
              .retrieve()
              .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
              .map(response -> response.get("reassigned_number"))
              .block();
      if (reassignedResponse == null) {
        throw new RuntimeException("No response from Twilio API");
      }
      logger.info(
          "Reassigned number details fetched successfully. Response: {}", reassignedResponse);
      Map<String, Object> responseMap = (Map<String, Object>) reassignedResponse;
      return processResponse(phoneNumber, responseMap);
    } catch (Exception e) {
      logger.error("Error fetching reassigned number details", e);
      throw new RuntimeException("Error fetching reassigned number details", e);
    }
  }

  /**
   * Processes the response from Twilio API to {@link RndApiResponse}.
   *
   * <p>If the response is null, it returns an error response with a 500 status code. If the
   * response contains an error code, it uses the {@code ERROR_MAP} to map the error code to an
   * appropriate error message and returns an error response. If there is no error, it returns a
   * response with the phone number and the is_number_reassigned status.
   *
   * @param phoneNumber the phone number used for the lookup
   * @param response the response from Twilio API
   * @return {@link RndApiResponse} containing the reassigned number details
   */
  private RndApiResponse processResponse(String phoneNumber, Map<String, Object> response) {
    // If response is null, return an error response
    if (response == null) {
      return new RndApiResponse(phoneNumber, null, new RndApiError(500, "Unknown error occurred"));
    }

    String isNumberReassignedString = (String) response.get("is_number_reassigned");
    String isNumberReassignedString = (String) response.get("is_number_reassigned");
    Boolean isNumberReassigned = null;
    if (isNumberReassignedString != null) {
      if ("yes".equalsIgnoreCase(isNumberReassignedString)) {
        isNumberReassigned = true;
      } else if ("no".equalsIgnoreCase(isNumberReassignedString)) {
        isNumberReassigned = false;
      }
    }

    Integer errorCode =
        response.get("error_code") == null ? null : (Integer) response.get("error_code");

    // If error code exists and is mapped, return an error response
    if (errorCode != null && ERROR_MAP.containsKey(errorCode)) {
      return new RndApiResponse(
          phoneNumber, isNumberReassigned, new RndApiError(errorCode, ERROR_MAP.get(errorCode)));
    }
    // No error, return response data
    return new RndApiResponse(phoneNumber, isNumberReassigned, null);
  }
}
