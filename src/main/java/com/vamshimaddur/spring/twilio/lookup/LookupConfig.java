package com.vamshimaddur.spring.twilio.lookup;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LookupConfig {
  private static final Logger logger = LoggerFactory.getLogger(LookupConfig.class);

  @Value("${twilio_account_sid}")
  private String accountSid;

  @Value("${twilio_auth_token}")
  private String authToken;

  /**
   * Initializes the Twilio client using the configured Account SID and Auth Token.
   *
   * <p>Validates the presence of Twilio credentials and throws an IllegalStateException if any
   * credentials are missing or blank. Logs the initialization process.
   */
  @PostConstruct
  public void init() {
    logger.info("Initializing Twilio with Account SID and Auth Token.");

    // Validate Twilio credentials
    if (accountSid == null || accountSid.isBlank()) {
      logger.error("Twilio Account SID is missing or blank.");
      throw new IllegalStateException("Twilio Account SID is not configured.");
    }
    if (authToken == null || authToken.isBlank()) {
      logger.error("Twilio Auth Token is missing or blank.");
      throw new IllegalStateException("Twilio Auth Token is not configured.");
    }

    // Initialize Twilio
    Twilio.init(accountSid, authToken);
    logger.info("Twilio initialized successfully.");
  }
}
