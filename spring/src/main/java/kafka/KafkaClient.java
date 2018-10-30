package kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaClient {

    public static void main(String[] args) {
        SpringApplication.run(KafkaClient.class, args);
    }
}