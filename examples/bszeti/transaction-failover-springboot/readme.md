DLQ messages after broker failover
==================================

This example tests an HA broker failover while a SpringBoot application actively receives and sends messags to the broker.

To run the example we need Apache Artemis donwloaded and set its path in the `activemq.basedir` propety for Maven. For example:

`export ARTEMIS_HOME=/Users/bszeti/tools/amq-broker-7.7.0`

Then we can run the test - with sending the log into `out.log` - like this (see `run.sh`):
```
export "MAVEN_OPTS=-Dorg.slf4j.simpleLogger.log.org.apache.activemq.artemis.jms.example=info"
export "MAVEN_OPTS=$MAVEN_OPTS -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.showThreadName=true"
export "MAVEN_OPTS=$MAVEN_OPTS -Dorg.slf4j.simpleLogger.logFile=out.log"
mvn clean install -Dactivemq.basedir=$ARTEMIS_HOME
```

The application is a simple JMS bridge that moves messages from a "source" queue to a "target" queue in a transacted way. The goal would be to achieve a seamless failover without lost or extra messages.

Steps:
- Two broker instances are started in HA mode with shared storage. The brokers' `max-delivery-attempts` is 3.
- The application uses the Qpid JMS AMQP client with the following connection string:

  `failover:(amqp://localhost:61616,amqp://localhost:61617)?failover.maxReconnectAttempts=16&jms.prefetchPolicy.all=5&jms.forceSyncSend=true`

- `org.messaginghub:pooled-jms` connection pool is used with 1 connection.
- First we send messages to our "source" queue (see property `send.count`),
- The `_AMQ_DUPL_ID` header is set to a UUID to utilize Artemis Duplicate Detection.
- Then the JMS consumers (`receive.concurrentConsumers`) are started.
- Messages are consumed transacted and sent to a `target` queue. The send still happens within the transaction.
- After 10% of the messages the broker failover is triggered. The JMS clients automatically reconnect to process the rest of the messages.
- The application is shut down when no more messages are incoming.

See `application.properties` for parameters.

Expected outcome:
- All the messages are moved to the `target` queue witohut message loss or duplicates.

Actual outcome:
- All the messages are moved to the `target` queue witohut message loss, but some messages are duplicated and end up on a message queue.

Notes:
- It's expected that some messages are redelivered again because the transactions are rolled back during a broker failover.
- But it's not clear how a message can be redelivered from `source` while it was already delivered to `target` as both the "acknowledgement" and the "send" participates in the same transaction.
- The extra redelivery should not even cause a problem - because of Duplicate Detection. It's usually a "silent" exception, but in transacted mode the exception reaches the client and causes message redelivery until _max-delivery-attempts_ is reached and the broker drops the message on the DLQ. See example `duplicate-detection-amqp` in the parent directory.
- At the end - while all the messages were moved to `target` queue succesfully - we get some "false alarm" messages on the DLQ.

See summary output of the application:
```
46097 [main] [INFO] Sent messages: 1000
46098 [main] [INFO] Message count on target queue: 1000
46098 [main] [WARNING] Duplicates on DLQ: 12
```
