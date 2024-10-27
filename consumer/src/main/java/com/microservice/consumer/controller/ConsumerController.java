package com.microservice.consumer.controller;

import com.microservice.consumer.model.DataEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consume")
public class ConsumerController {
    private static final Logger logger = LogManager.getLogger(ConsumerController.class);
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<String> consume(@RequestBody DataEntity data) {
        logger.info("Consuming data: {}", data);
        return ResponseEntity.ok("Data consumed");
    }
}