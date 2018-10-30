# Getting Started with Apache Kafka

This repository contains the demo for the DevNation Live tech talk about Getting Started with Apache Kafka.

## Clone this repository

* Clone this repository
    * `git clone https://github.com/scholzj/getting-started-with-apache-kafka.git`
    * `cd getting-started-with-apache-kafka`

## Download and installing Kafka

* Download Apache Kafka from [http://kafka.apache.org/downloads](http://kafka.apache.org/downloads)
    * `curl -LO https://www.apache.org/dist/kafka/2.0.0/kafka_2.11-2.0.0.tgz`
* Unpack it
    * `tar -xzf kafka_2.11-2.0.0.tgz`
* Go into the KAfka directory
    * `cd kafka_2.11-2.0.0`

## Start Zookeeper and Kafka

* Zookeeper needs to be started first:
    * `bin/zookeeper-server-start.sh config/zookeeper.properties`
* Kafka can be started second
    * `bin/kafka-server-start.sh config/server.properties`

## Create a topic

* Create a topic called `devnation` with 3 partitions and 3 replicas
    * `bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic devnation --partitions 3 --replication-factor 1`

## Send and receive messages using CLI

* As a quick check, you can check that sending and receiving messages works from command line
* `echo "Hello World from CLI" | bin/kafka-console-producer.sh --broker-list localhost:9092 --topic dev-nation`
* `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic devnation --max-messages 1`

## Send and receive messages using Java

* Go the the directory with Kafka clients
    * `cd ../java`
* Start the Kafka consumer
    * `mvn compile exec:java -Dexec.mainClass="kafka.MessageConsumer"`
* Start the Kafka producer
    * `mvn compile exec:java -Dexec.mainClass="kafka.MessageProducer"`

## Send and receive messages using Spring

* Go the the directory with Spring
    * `cd ../spring`
* Run the Spring application
    * `mvn spring-boot:run`
* Send messages using the REST API
    * `curl -sv -X POST -H "Content-Type: text/plain" http://localhost:8080/kafka/ --data 'Hello World from Spring'`
* Check the logs for received messages

## Send and receive messages using Python

* Go the the directory with Kafka clients
    * `cd ../python`
* Install the Kafka client
    * `pip install confluent-kafka`
* Start the Kafka consumer
    * `python consumer.py`
* Start the Kafka producer
    * `python producer.py`

## Send and receive messages using Javascript

* Go the the directory with Kafka clients
    * `cd ../node.js`
* Install the `node-rdkafka` client
    * `npm install node-rdkafka`
* Start the Kafka consumer
    * `node consumer.js`
* Start the Kafka producer
    * `node producer.js`