# log-ingest

This project demonstrates a microservices architecture using Spring Boot, with two services (`producer` and `consumer`), a Eureka server for service discovery, and a Logstash configuration for ingesting logs into Elasticsearch.

## Overview

The application consists of:
- **Discovery Server**: Acts as the Eureka server for service discovery, allowing microservices to register and communicate.
- **Producer Microservice**: Generates data and verifies if the `code` field is numeric before allowing the `consumer` to consume it.
- **Consumer Microservice**: Consumes data from the `producer` if the `code` is valid.
- **Logstash**: Configured to ingest logs from both microservices and send them to Elasticsearch for storage and analysis.

## Getting Started

### Prerequisites
- Java 17
- Spring Boot 3.x
- Maven
- Elasticsearch (must be already running)

### Running the Application

1. **Start the Discovery Server**:
    - Run the `DiscoveryApplication` class to launch the service discovery server.

2. **Start the Microservices**:
    - Run the `ProducerApplication` to start the producer microservice.
    - Run the `ConsumerApplication` to start the consumer microservice.

Both microservices will automatically register with the Discovery server, enabling service discovery and communication.

### Logstash Configuration

To ingest logs from the microservices into Elasticsearch, use the following Logstash configuration:

```plaintext
input {
  file {
    path => "/path/to/logs/*.log" # Replace with the actual log file path
    start_position => "beginning"
    sincedb_path => "NUL" # For Windows, use "NUL" to disable sincedb
    codec => "plain"
  }
}

filter {
  grok {
    match => { "message" => "%{TIMESTAMP_ISO8601:timestamp} \[%{WORD:scope}\] %{LOGLEVEL:log_level} %{JAVACLASS:class} - %{GREEDYDATA:message_content}" }
  }
  
  date {
    match => [ "timestamp", "ISO8601" ]
    target => "@timestamp"
    remove_field => [ "timestamp" ]
  }

  mutate {
    add_field => { "service_scope" => "%{scope}" }
    remove_field => [ "scope" ]
  }

  if [message_content] =~ /Verifying if code/ {
    grok {
      match => { "message_content" => "Verifying if code '%{WORD:code}' is numeric: %{WORD:is_numeric}" }
    }
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"] # Replace with your Elasticsearch host if needed
    index => "microservice-logs-%{+YYYY.MM.dd}"
  }
  
  stdout {
    codec => rubydebug
  }
}
