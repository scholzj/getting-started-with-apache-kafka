package kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "devnation", groupId = "devnation-spring")
    public void listener(@Payload(required = false) String value,
                                @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key) {
        LOG.info("Received message:"
                + "\n\t Key: " + key
                + "\n\t Value: " + value);
    }
}
