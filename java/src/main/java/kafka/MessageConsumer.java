package kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MessageConsumer implements Runnable {
    private final Duration TIMEOUT = Duration.ofSeconds(1);
    private final KafkaConsumer<String, String> consumer;
    private final CountDownLatch latch;
    private boolean stopConsumer = false;

    public MessageConsumer(CountDownLatch latch)    {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "devnation-java");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.latch = latch;

        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("devnation"));
    }

    @Override
    public void run() {
        while (!stopConsumer)
        {
            ConsumerRecords<String, String> records = consumer.poll(TIMEOUT);

            if(records.isEmpty()) {
                continue;
            }

            for (ConsumerRecord<String, String> record : records)
            {
                System.out.println("-I- received message:" +
                        "\n\t Topic: " + record.topic() +
                        "\n\t Partition: " + record.partition() +
                        "\n\t Key: " + record.key() +
                        "\n\t Value: " + record.value());

                latch.countDown();
            }
        }

        consumer.close();
    }

    public void stopConsumer()  {
        stopConsumer = true;
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");

        CountDownLatch latch = new CountDownLatch(1000);

        MessageConsumer consumer = new MessageConsumer(latch);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        latch.await();
        consumer.stopConsumer();
        consumerThread.join();
    }
}