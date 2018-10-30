package kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/kafka")
public class HttpController {
    final Logger LOG = LoggerFactory.getLogger(HttpController.class);

    @Autowired
    KafkaProducer kafkaProducer;

    @PostMapping
    public String post(@RequestBody String text){
        LOG.info("Received POST request {}", text);
        kafkaProducer.sendMessage(text);
        return text;
    }
}
