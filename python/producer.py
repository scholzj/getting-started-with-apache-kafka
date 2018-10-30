from confluent_kafka import Producer
import sys

def delivery_callback(err, msg):
    if err:
        sys.stderr.write('-I- Message failed delivery: %s\n' % err)
    else:
        sys.stderr.write('-I- Message delivered to %s [%d]\n' % (msg.topic(), msg.partition()))

if __name__ == '__main__':
    conf = {
            'bootstrap.servers': 'localhost:9092'
        }

    p = Producer(**conf)

    counter = 1

    while True:
        msg = 'Hello World from Python ' + str(counter)
        sys.stderr.write('-I- Sedning message: %d\n' % counter)
        p.produce('devnation', msg, callback=delivery_callback)
        counter += 1
        p.poll(0)

        if counter >= 10:
            break

    sys.stderr.write('%% Waiting for %d deliveries\n' % len(p))
    p.flush()
