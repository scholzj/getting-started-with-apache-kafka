var Kafka = require('node-rdkafka');

var producer = new Kafka.Producer({
    'metadata.broker.list': 'localhost:9092',
    'dr_cb': true  //delivery report callback
});

var topicName = 'devnation';

//logging debug messages, if debug is enabled
producer.on('event.log', function(log) {
    console.log(log);
});

//logging all errors
producer.on('event.error', function(err) {
    console.error('-E- Error from producer:');
    console.error(err);
});

//counter to stop this sample after maxMessages are sent
var counter = 0;
var maxMessages = 10;

producer.on('delivery-report', function(err, report) {
    console.log('-I- Delivery report received:'
        + '\n\t Topic: ' + report.topic
        + '\n\t Partition: ' + report.partition
        + '\n\t Offset: ' + report.offset
        + '\n\t Key: ' + report.key);

    counter++;
});

producer.on('ready', function(arg) {
  console.log('-I- Producer ready');

  for (var i = 0; i < maxMessages; i++) {
    var value = Buffer.from('Hello World from JavaScript ' + i);
    var key = null;
    var partition = -1; // if partition is set to -1, librdkafka will use the default partitioner
    producer.produce(topicName, partition, value, key);
  }

  //need to keep polling for a while to ensure the delivery reports are received
  var pollLoop = setInterval(function() {
      producer.poll();
      if (counter === maxMessages) {
        clearInterval(pollLoop);
        producer.disconnect();
      }
    }, 1000);

});

producer.on('disconnected', function(arg) {
    console.log('-I- Producer disconnected');
});

//starting the producer
producer.connect();