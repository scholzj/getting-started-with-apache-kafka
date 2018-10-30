from confluent_kafka import Consumer, KafkaException, KafkaError
import sys
import logging
from pprint import pformat

def print_assignment(consumer, partitions):
    print('Assignment:', partitions)

if __name__ == '__main__':
    conf = {
            'bootstrap.servers': 'localhost:9092', 
            'group.id': 'devnation-python', 
            'session.timeout.ms': 6000,
            'auto.offset.reset': 'earliest'
        }

    c = Consumer(conf)
    c.subscribe(['devnation'], on_assign=print_assignment)

    # Read messages from Kafka, print to stdout
    try:
        while True:
            msg = c.poll(timeout=1.0)
            if msg is None:
                continue
            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    # Continue -> we reached the end of the partition
                    continue
                else:
                    sys.stderr.write('-E- Something went wrong: %s' % msg.error())
                    break
            else:
                # Proper message
                sys.stderr.write('-I- %s [%d] at offset %d with key %s: ' %
                                 (msg.topic(), msg.partition(), msg.offset(),
                                  str(msg.key())))
                print(msg.value())

    except KeyboardInterrupt:
        sys.stderr.write('%% Aborted by user\n')

    finally:
        c.close()
