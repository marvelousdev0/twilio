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
  private final WebClient webClient;

  public LookupService(LookupConfig lookupConfig) {
    logger.info("Initializing Twilio Lookup Service.");
    String basicAuth = lookupConfig.init();
    this.webClient =
        WebClient.builder()
            .baseUrl(
                "https://lookups.twilio.com/v2/PhoneNumbers/2345678904?Fields=reassigned_number&LastVerifiedDate=20240101")
            .defaultHeader("Authorization", "Basic " + basicAuth)
            .build();
  }

  public Map<String, Object> fetchReassignedNumberDetails() {
    return webClient
        .get()
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }
}
