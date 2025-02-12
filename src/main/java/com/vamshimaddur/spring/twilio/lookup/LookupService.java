package com.vamshimaddur.spring.twilio.lookup;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LookupService {
  private static final Logger logger = LoggerFactory.getLogger(LookupService.class);
  private final WebClient.Builder webClientBuilder;
  private final LookupConfig lookupConfig;

  public LookupService(WebClient.Builder webClientBuilder, LookupConfig lookupConfig) {
    this.webClientBuilder = webClientBuilder;
    this.lookupConfig = lookupConfig;
  }

  public Object fetchReassignedNumberDetails(String phoneNumber, String lastVerifiedDate) {
    logger.info(
        "Fetching reassigned number details for phone number: {} and lastVerifiedDate: {}",
        phoneNumber,
        lastVerifiedDate);
    String basicAuth = lookupConfig.init();

    WebClient webClient =
        webClientBuilder
            .baseUrl("https://lookups.twilio.com/v2")
            .defaultHeader("Authorization", "Basic " + basicAuth)
            .build();

    return webClient
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
  }
}
