package kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    private String kafkaTopic = "devnation";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String text) {
        LOG.info("Sending message");
        kafkaTemplate.send(kafkaTopic, text);
    }
}
