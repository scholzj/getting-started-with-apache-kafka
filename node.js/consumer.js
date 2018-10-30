var Kafka = require('node-rdkafka');

var consumer = new Kafka.KafkaConsumer({
    'metadata.broker.list': 'localhost:9092',
    'group.id': 'devnation-nodejs',
    'enable.auto.commit': true
});

var topicName = 'devnation';

//logging all errors
consumer.on('event.error', function(err) {
    console.error('-E- Error from consumer:');
    console.error(err);
});

consumer.on('ready', function(arg) {
    console.log('-I- Consumer ready');

    consumer.subscribe([topicName]);
    consumer.consume();
});

consumer.on('data', function(m) {
    console.log('-I- Received message:'
        + '\n\t Topic: ' + m.topic
        + '\n\t Partition: ' + m.partition
        + '\n\t Offset: ' + m.offset
        + '\n\t Key: ' + m.key
        + '\n\t Value: ' + m.value.toString())
});

consumer.on('disconnected', function(arg) {
    console.log('-I- Consumer disconnected')
});

// Connect the cosumer
consumer.connect();

// Run the conusmer for 300s
setTimeout(function() {
    consumer.disconnect();
}, 300000);