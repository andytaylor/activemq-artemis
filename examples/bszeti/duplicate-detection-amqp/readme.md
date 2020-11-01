This is example to test the unexpected exception "Duplicate message detected" error during transacted sending.

To run the example set "activemq.basedir" property point to your $ARTEMIS_HOME. For example:
`mvn clean install -Dactivemq.basedir=/Users/bszeti/tools/amq-broker-7.7.0`

Stepsin the test:
- Start an Artemis broker on port 61616
- Connect to broker with AMQP protocol (qpid jms client)
- Send two messages with the same `_AMQ_DUPL_ID` header using a non transacted session
- Verify that only one message exists on the queue
- Send two messages with the same `_AMQ_DUPL_ID` header using a transacted session - in two different trasactions.
- An unexpected "Duplicate message detected" exception is thrown during `session.commit()`. This exception probably should not reach the client - just like in case of the non-transacted mode.

This can cause problems during a broker HA failover. If a broker failover happens while a jms client is processing a transacted receive the broker may decide to rollback the transaction and resend the message again. With an "JMS bridge" application receving messages from a queue and sending messages to another queue - on the same broker - within the same transaction it can cause unexpected message duplicates. In this case the message has arrived on the target queue before the failover but it was still redelivered and caused the exception - eventually sending the duplicated message to DLQ.



