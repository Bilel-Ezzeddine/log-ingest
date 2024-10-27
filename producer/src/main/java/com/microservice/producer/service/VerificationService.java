package com.microservice.producer.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    private static final Logger logger = LogManager.getLogger(VerificationService.class);

    public boolean isNumeric(String code) {
        boolean result = code != null && code.matches("\\d+");
        logger.debug("Verifying if code '{}' is numeric: {}", code, result);
        return result;
    }
}

