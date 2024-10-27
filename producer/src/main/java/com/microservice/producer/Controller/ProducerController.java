package com.microservice.producer.Controller;

import com.microservice.producer.model.DataEntity;
import com.microservice.producer.service.VerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/produce")
public class ProducerController {
    private static final Logger logger = LogManager.getLogger(ProducerController.class);

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<String> produce(@RequestBody DataEntity data) {
        logger.info("Received data to produce: {}", data);
        if (verificationService.isNumeric(data.getCode())) {
            logger.info("Code is numeric. Sending data to Consumer.");
            try {
                restTemplate.postForObject("http://localhost:8091/consume", data, String.class);
                logger.info("Data successfully sent to Consumer.");
                return ResponseEntity.ok("Data sent to consumer");
            } catch (Exception e) {
                logger.error("Failed to send data to Consumer: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send data to Consumer");
            }
        } else {
            logger.warn("Code '{}' is not numeric. Data will not be sent.", data.getCode());
            return ResponseEntity.badRequest().body("Code is not a valid numeric value");
        }
    }
}

