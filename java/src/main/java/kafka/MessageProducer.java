package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MessageProducer implements Runnable {
    private final KafkaProducer<String, String> producer;
    private final CountDownLatch latch;
    private boolean stopProducer = false;

    public MessageProducer(CountDownLatch latch)    {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        this.latch = latch;

        producer = new KafkaProducer<String, String>(props);
    }

    @Override
    public void run() {
        int counter = 0;

        while (!stopProducer)    {
            ProducerRecord record = new ProducerRecord<String, String>("devnation", "Hello World from Java " + counter);
            Future<RecordMetadata> futureResult = producer.send(record);

            try {
                RecordMetadata result = futureResult.get();

                System.out.println("-I- message sent:" +
                        "\n\t Topic: " + record.topic() +
                        "\n\t Partition: " + record.partition() +
                        "\n\t Offset: " + result.offset() +
                        "\n\t Key: " + record.key() +
                        "\n\t Value: " + record.value());

                counter++;
                latch.countDown();

            } catch (InterruptedException|ExecutionException e) {
                System.out.println("-E- Failed to send a message: " + e);
                e.printStackTrace();
                break;
            }
        }

        producer.close();
    }

    public void stopProducer()  {
        stopProducer = true;
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");

        CountDownLatch latch = new CountDownLatch(10);

        MessageProducer producer = new MessageProducer(latch);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        latch.await();
        producer.stopProducer();
        producerThread.join();
    }
}