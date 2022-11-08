Name | Description | Default | XML Name
---|---|---|---
criticalAnalyzerCheckPeriod | The timeout here will be defaulted to half critical-analyzer-timeout, calculation happening at runtime | 0 | critical-analyzer-check-period
pageMaxConcurrentIO | The max number of concurrent reads allowed on paging | 5 | page-max-concurrent-io
messageCounterSamplePeriod | the sample period (in ms) to use for message counters | 10000 | message-counter-sample-period
networkCheckNIC | The network interface card name to be used to validate the address. | n/a | network-check-nic
globalMaxSize | Size (in bytes) before all addresses will enter into their Full Policy configured upon messages being                  produced.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | -1 | global-max-size
journalFileSize | The size (in bytes) of each journal file.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 10485760 | journal-file-size
configurationFileRefreshPeriod | how often (in ms) to check the configuration file for modifications | 5000 | configuration-file-refresh-period
diskScanPeriod | how often (in ms) to scan the disks for full disks. | 5000 | disk-scan-period
journalRetentionDirectory | the directory to store journal-retention message in and rention configuraion. | n/a | journal-retention-directory
networkCheckPeriod | A frequency in milliseconds to how often we should check if the network is still up | 10000 | network-check-period
journalBufferSize_AIO | The size (in bytes) of the internal buffer on the journal.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 501760 | journal-buffer-size
networkCheckURLList | A comma separated list of URLs to be used to validate if the broker should be kept up | n/a | network-check-URL-list
networkCheckTimeout | A timeout used in milliseconds to be used on the ping. | 1000 | network-check-timeout
pageSyncTimeout | The timeout (in nanoseconds) used to sync pages. The exact default value                  depend on whether the journal is ASYNCIO or NIO. | n/a | page-sync-timeout
journalPoolFiles | how many journal files to pre-create | -1 | journal-pool-files
criticalAnalyzer | should analyze response time on critical paths and decide for broker log, shutdown or halt. | true | critical-analyzer
readWholePage | Whether the whole page is read while getting message after page cache is evicted. | false | read-whole-page
maxDiskUsage | Max percentage of disk usage before the system blocks or fails clients. | 90 | max-disk-usage
globalMaxMessages | Number of messages before all addresses will enter into their Full Policy configured.                  It works in conjunction with global-max-size, being watever value hits its maximum first. | -1 | global-max-messages
internalNamingPrefix | Artemis uses internal queues and addresses for implementing certain behaviours.  These queues and addresses                  will be prefixed by default with "$.activemq.internal" to avoid naming clashes with user namespacing.                  This can be overridden by setting this value to a valid Artemis address. | n/a | internal-naming-prefix
journalFileOpenTimeout | the length of time in seconds to wait when opening a new Journal file before timing out and failing | 5 | journal-file-open-timeout
journalCompactPercentage | The percentage of live data on which we consider compacting the journal | 30 | journal-compact-percentage
createBindingsDir | true means that the server will create the bindings directory on start up | true | create-bindings-dir
suppressSessionNotifications | Whether or not to suppress SESSION_CREATED and SESSION_CLOSED notifications.                  Set to true to reduce notification overhead. However, these are required to                  enforce unique client ID utilization in a cluster for MQTT clients. | false | suppress-session-notifications
journalBufferTimeout_AIO | The timeout (in nanoseconds) used to flush internal buffers on the journal. The exact default value                  depend on whether the journal is ASYNCIO or NIO. | n/a | journal-buffer-timeout
journalType | the type of journal to use | ASYNCIO | journal-type
name | Node name. If set, it will be used in topology notifications. | n/a | name
networkCheckPingCommand | The ping command used to ping IPV4 addresses. | n/a | network-check-ping-command
temporaryQueueNamespace | the namespace to use for looking up address settings for temporary queues | n/a | temporary-queue-namespace
pagingDirectory | the directory to store paged messages in | data/paging | paging-directory
journalDirectory | the directory to store the journal files in | data/journal | journal-directory
journalBufferSize_NIO | The size (in bytes) of the internal buffer on the journal.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 501760 | journal-buffer-size
journalDeviceBlockSize | The size in bytes used by the device. This is usually translated as fstat/st_blksize                  And this is a way to bypass the value returned as st_blksize. | n/a | journal-device-block-size
nodeManagerLockDirectory | the directory to store the node manager lock file | n/a | node-manager-lock-directory
messageCounterMaxDayHistory | how many days to keep message counter history | 10 | message-counter-max-day-history
largeMessagesDirectory | the directory to store large messages | data/largemessages | large-messages-directory
networkCheckPing6Command | The ping command used to ping IPV6 addresses. | n/a | network-check-ping6-command
memoryWarningThreshold | Percentage of available memory which will trigger a warning log | 25 | memory-warning-threshold
mqttSessionScanInterval | how often (in ms) to scan for expired MQTT sessions | 5000 | mqtt-session-scan-interval
journalMaxAtticFiles |  | n/a | journal-max-attic-files
journalSyncTransactional | if true wait for transaction data to be synchronized to the journal before returning response to                  client | true | journal-sync-transactional
logJournalWriteRate | Whether to log messages about the journal write rate | false | log-journal-write-rate
journalMaxIO_AIO | the maximum number of write requests that can be in the AIO queue at any one time. Default is 500 for                  AIO and 1 for NIO. | n/a | journal-max-io
messageExpiryScanPeriod | how often (in ms) to scan for expired messages | 30000 | message-expiry-scan-period
criticalAnalyzerTimeout | The default timeout used on analyzing timeouts on the critical path. | 120000 | critical-analyzer-timeout
messageCounterEnabled | true means that message counters are enabled | false | message-counter-enabled
journalCompactMinFiles | The minimal number of data files before we can start compacting | 10 | journal-compact-min-files
createJournalDir | true means that the journal directory will be created | true | create-journal-dir
addressQueueScanPeriod | how often (in ms) to scan for addresses and queues that need to be deleted | 30000 | address-queue-scan-period
memoryMeasureInterval | frequency to sample JVM memory in ms (or -1 to disable memory sampling) | -1 | memory-measure-interval
journalSyncNonTransactional | if true wait for non transaction data to be synced to the journal before returning response to client. | true | journal-sync-non-transactional
connectionTtlCheckInterval | how often (in ms) to check connections for ttl violation | 2000 | connection-ttl-check-interval
rejectEmptyValidatedUser | true means that the server will not allow any message that doesn't have a validated user, in JMS this is JMSXUserID | false | reject-empty-validated-user
journalMaxIO_NIO | the maximum number of write requests that can be in the AIO queue at any one time. Default is 500 for                  AIO and 1 for NIO.Currently Broker properties only supports using an integer and measures in bytes | n/a | journal-max-io
transactionTimeoutScanPeriod | how often (in ms) to scan for timeout transactions | 1000 | transaction-timeout-scan-period
systemPropertyPrefix | This defines the prefix which we will use to parse System properties for the configuration. Default= | n/a | system-property-prefix
transactionTimeout | how long (in ms) before a transaction can be removed from the resource manager after create time | 300000 | transaction-timeout
journalLockAcquisitionTimeout | how long (in ms) to wait to acquire a file lock on the journal | -1 | journal-lock-acquisition-timeout
journalBufferTimeout_NIO | The timeout (in nanoseconds) used to flush internal buffers on the journal. The exact default value                  depend on whether the journal is ASYNCIO or NIO. | n/a | journal-buffer-timeout
journalMinFiles | how many journal files to pre-create | 2 | journal-min-files
**bridgeConfigurations** |  | 
bridgeConfigurations . ***name*** .retryIntervalMultiplier | multiplier to apply to successive retry intervals | 1 | retry-interval-multiplier
bridgeConfigurations . ***name*** .maxRetryInterval | Limit to the retry-interval growth (due to retry-interval-multiplier) | 2000 | max-retry-interval
bridgeConfigurations . ***name*** .filterString |  | n/a | filter-string
bridgeConfigurations . ***name*** .connectionTTL | how long to keep a connection alive in the absence of any data arriving from the client. This should                  be greater than the ping period. | 60000 | connection-ttl
bridgeConfigurations . ***name*** .confirmationWindowSize | Once the bridge has received this many bytes, it sends a confirmation.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 1048576 | confirmation-window-size
bridgeConfigurations . ***name*** .staticConnectors |  | n/a | static-connectors
bridgeConfigurations . ***name*** .reconnectAttemptsOnSameNode |  | n/a | reconnect-attempts-on-same-node
bridgeConfigurations . ***name*** .concurrency | Number of concurrent workers, more workers can help increase throughput on high latency networks.                  Defaults to 1 | 1 | concurrency
bridgeConfigurations . ***name*** .transformerConfiguration |  | n/a | transformer-configuration
bridgeConfigurations . ***name*** .transformerConfiguration .className |  | n/a | class-name
bridgeConfigurations . ***name*** .transformerConfiguration .properties | A KEY/VALUE pair to set on the transformer, i.e. ...properties.MY_PROPERTY=MY_VALUE | n/a | property
bridgeConfigurations . ***name*** .password | password, if unspecified the cluster-password is used | n/a | password
bridgeConfigurations . ***name*** .queueName | name of queue that this bridge consumes from | n/a | queue-name
bridgeConfigurations . ***name*** .forwardingAddress | address to forward to. If omitted original address is used | n/a | forwarding-address
bridgeConfigurations . ***name*** .routingType | how should the routing-type on the bridged messages be set? | PASS | routing-type
bridgeConfigurations . ***name*** .name | unique name for this bridge | n/a | name
bridgeConfigurations . ***name*** .ha | whether this bridge supports fail-over | false | ha
bridgeConfigurations . ***name*** .initialConnectAttempts | maximum number of initial connection attempts, -1 means 'no limits' | -1 | initial-connect-attempts
bridgeConfigurations . ***name*** .retryInterval | period (in ms) between successive retries | 2000 | retry-interval
bridgeConfigurations . ***name*** .producerWindowSize | Producer flow control.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 1048576 | producer-window-size
bridgeConfigurations . ***name*** .clientFailureCheckPeriod | The period (in milliseconds) a bridge's client will check if it failed to receive a ping from the                  server. -1 disables this check. | 30000 | check-period
bridgeConfigurations . ***name*** .discoveryGroupName |  | n/a | discovery-group-ref
bridgeConfigurations . ***name*** .user | username, if unspecified the cluster-user is used | n/a | user
bridgeConfigurations . ***name*** .useDuplicateDetection | should duplicate detection headers be inserted in forwarded messages? | true | use-duplicate-detection
bridgeConfigurations . ***name*** .minLargeMessageSize | Any message larger than this size (in bytes) is considered a large message (to be sent in                  chunks).                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 102400 | min-large-message-size
**AMQPConnections** |  | 
AMQPConnections . ***name*** .reconnectAttempts | How many attempts should be made to reconnect after failure | -1 | reconnect-attempts
AMQPConnections . ***name*** .password | Password used to connect. If not defined it will try an anonymous connection. | n/a | password
AMQPConnections . ***name*** .retryInterval | period (in ms) between successive retries | 5000 | retry-interval
AMQPConnections . ***name*** .connectionElements | An AMQP Broker Connection that supports 4 types, these are:1. Mirrors - The broker uses an AMQP connection to another broker and duplicates messages and sends acknowledgements over the wire.2. Senders - Messages received on specific queues are transferred to another endpoint.3. Receivers - The broker pulls messages from another endpoint.4. Peers - The broker creates both senders and receivers on another endpoint that knows how to handle them. This is currently implemented by Apache Qpid Dispatch.Currently only mirror type is supported | n/a | amqp-connection
AMQPConnections . ***name*** .connectionElements . ***name*** .messageAcknowledgements | If true then message acknowledgements will be mirrored | n/a | message-acknowledgements
AMQPConnections . ***name*** .connectionElements . ***name*** .queueRemoval | Should mirror queue deletion events for addresses and queues. | n/a | queue-removal
AMQPConnections . ***name*** .connectionElements . ***name*** .addressFilter | This defines a filter that mirror will use to determine witch events will be forwarded toward               target server based on source address. | n/a | address-filter
AMQPConnections . ***name*** .connectionElements . ***name*** .queueCreation | Should mirror queue creation events for addresses and queues. | n/a | queue-creation
AMQPConnections . ***name*** .autostart | should the broker connection be started when the server is started. | true | auto-start
AMQPConnections . ***name*** .user | User name used to connect. If not defined it will try an anonymous connection. | n/a | user
AMQPConnections . ***name*** .uri | uri of the amqp connection | n/a | uri
**divertConfiguration** |  | 
divertConfiguration .transformerConfiguration |  | n/a | transformer-configuration
divertConfiguration .transformerConfiguration .className |  | n/a | class-name
divertConfiguration .transformerConfiguration .properties | A KEY/VALUE pair to set on the transformer, i.e. ...properties.MY_PROPERTY=MY_VALUE | n/a | property
divertConfiguration .filterString |  | n/a | filter-string
divertConfiguration .routingName | the routing name for the divert | n/a | routing-name
divertConfiguration .address | the address this divert will divert from | n/a | address
divertConfiguration .forwardingAddress | the forwarding address for the divert | n/a | forwarding-address
divertConfiguration .routingType | how should the routing-type on the diverted messages be set? | n/a | routing-type
divertConfiguration .exclusive | whether this is an exclusive divert | false | exclusive
**addressSettings** |  | 
addressSettings . ***address*** .configDeleteDiverts |  | n/a | config-delete-addresses
addressSettings . ***address*** .expiryQueuePrefix |  | n/a | expiry-queue-prefix
addressSettings . ***address*** .defaultConsumerWindowSize |  | n/a | default-consumer-window-size
addressSettings . ***address*** .maxReadPageBytes |  | n/a | max-read-page-bytes
addressSettings . ***address*** .deadLetterQueuePrefix |  | n/a | dead-letter-queue-prefix
addressSettings . ***address*** .defaultGroupRebalancePauseDispatch |  | n/a | default-group-rebalance-pause-dispatch
addressSettings . ***address*** .autoCreateAddresses |  | n/a | auto-create-addresses
addressSettings . ***address*** .slowConsumerThreshold |  | n/a | slow-consumer-threshold
addressSettings . ***address*** .managementMessageAttributeSizeLimit |  | n/a | management-message-attribute-size-limit
addressSettings . ***address*** .autoCreateExpiryResources |  | n/a | auto-create-expiry-resources
addressSettings . ***address*** .pageSizeBytes |  | n/a | page-size-bytes
addressSettings . ***address*** .minExpiryDelay |  | n/a | min-expiry-delay
addressSettings . ***address*** .defaultConsumersBeforeDispatch |  | n/a | default-consumers-before-dispatch
addressSettings . ***address*** .expiryQueueSuffix |  | n/a | expiry-queue-suffix
addressSettings . ***address*** .configDeleteQueues |  | n/a | config-delete-queues
addressSettings . ***address*** .enableIngressTimestamp |  | n/a | enable-ingress-timestamp
addressSettings . ***address*** .autoDeleteCreatedQueues |  | n/a | auto-delete-created-queues
addressSettings . ***address*** .expiryAddress |  | n/a | expiry-address
addressSettings . ***address*** .managementBrowsePageSize |  | n/a | management-browse-page-size
addressSettings . ***address*** .autoDeleteQueues |  | n/a | auto-delete-queues
addressSettings . ***address*** .retroactiveMessageCount |  | n/a | retroactive-message-count
addressSettings . ***address*** .maxExpiryDelay |  | n/a | max-expiry-delay
addressSettings . ***address*** .maxDeliveryAttempts |  | n/a | max-delivery-attempts
addressSettings . ***address*** .defaultGroupFirstKey |  | n/a | default-group-first-key
addressSettings . ***address*** .slowConsumerCheckPeriod |  | n/a | slow-consumer-check-period
addressSettings . ***address*** .defaultPurgeOnNoConsumers |  | n/a | default-purge-on-no-consumers
addressSettings . ***address*** .defaultLastValueKey |  | n/a | default-last-value-key
addressSettings . ***address*** .autoCreateQueues |  | n/a | auto-create-queues
addressSettings . ***address*** .defaultExclusiveQueue |  | n/a | default-exclusive-queue
addressSettings . ***address*** .defaultMaxConsumers |  | n/a | default-max-consumers
addressSettings . ***address*** .defaultQueueRoutingType |  | n/a | default-queue-routing-type
addressSettings . ***address*** .messageCounterHistoryDayLimit |  | n/a | message-counter-history-day-limit
addressSettings . ***address*** .defaultGroupRebalance |  | n/a | default-group-rebalance
addressSettings . ***address*** .defaultAddressRoutingType |  | n/a | default-address-routing-type
addressSettings . ***address*** .maxSizeBytesRejectThreshold |  | n/a | max-size-bytes-reject-threshold
addressSettings . ***address*** .pageCacheMaxSize |  | n/a | page-cache-max-size
addressSettings . ***address*** .autoCreateDeadLetterResources |  | n/a | auto-create-dead-letter-resources
addressSettings . ***address*** .maxRedeliveryDelay |  | n/a | max-redelivery-delay
addressSettings . ***address*** .configDeleteAddresses |  | n/a | config-delete-addresses
addressSettings . ***address*** .deadLetterAddress |  | n/a | dead-letter-address
addressSettings . ***address*** .autoDeleteQueuesMessageCount |  | n/a | auto-delete-queues-message-count
addressSettings . ***address*** .autoDeleteAddresses |  | n/a | auto-delete-addresses
addressSettings . ***address*** .addressFullMessagePolicy |  | n/a | address-full-message-policy
addressSettings . ***address*** .maxSizeBytes |  | n/a | max-size-bytes
addressSettings . ***address*** .defaultDelayBeforeDispatch |  | n/a | default-delay-before-dispatch
addressSettings . ***address*** .redistributionDelay |  | n/a | redistribution-delay
addressSettings . ***address*** .maxSizeMessages |  | n/a | max-size-messages
addressSettings . ***address*** .redeliveryMultiplier |  | n/a | redelivery-multiplier
addressSettings . ***address*** .defaultRingSize |  | n/a | default-ring-size
addressSettings . ***address*** .defaultLastValueQueue |  | n/a | default-last-value-queue
addressSettings . ***address*** .slowConsumerPolicy |  | n/a | slow-consumer-policy
addressSettings . ***address*** .redeliveryCollisionAvoidanceFactor |  | n/a | redelivery-collision-avoidance-factor
addressSettings . ***address*** .autoDeleteQueuesDelay |  | n/a | auto-delete-queues-delay
addressSettings . ***address*** .autoDeleteAddressesDelay |  | n/a | auto-delete-addresses-delay
addressSettings . ***address*** .expiryDelay |  | n/a | expiry-delay
addressSettings . ***address*** .enableMetrics |  | n/a | enable-metrics
addressSettings . ***address*** .sendToDLAOnNoRoute |  | n/a | send-to-d-l-a-on-no-route
addressSettings . ***address*** .slowConsumerThresholdMeasurementUnit |  | n/a | slow-consumer-threshold-measurement-unit
addressSettings . ***address*** .queuePrefetch |  | n/a | queue-prefetch
addressSettings . ***address*** .redeliveryDelay |  | n/a | redelivery-delay
addressSettings . ***address*** .deadLetterQueueSuffix |  | n/a | dead-letter-queue-suffix
addressSettings . ***address*** .defaultNonDestructive |  | n/a | default-non-destructive
**federationConfigurations** |  | 
federationConfigurations . ***name*** .transformerConfigurations | optional transformer configuration | n/a | transformer
federationConfigurations . ***name*** .transformerConfigurations . ***name*** .transformerConfiguration | Allows adding a custom transformer to amend the message | n/a | transformer
federationConfigurations . ***name*** .transformerConfigurations . ***name*** .transformerConfiguration . ***name*** .className | the class name of the Transformer implementation | n/a | class-name
federationConfigurations . ***name*** .transformerConfigurations . ***name*** .transformerConfiguration . ***name*** .properties | A KEY/VALUE pair to set on the transformer, i.e. ...properties.MY_PROPERTY=MY_VALUE | n/a | property
federationConfigurations . ***name*** .queuePolicies |  | n/a | queue-policy
federationConfigurations . ***name*** .queuePolicies . ***name*** .priorityAdjustment | when a consumer attaches its priority is used to make the upstream consumer, but with an adjustment by default -1,               so that local consumers get load balanced first over remote, this enables this to be configurable should it be wanted/needed. | n/a | priority-adjustment
federationConfigurations . ***name*** .queuePolicies . ***name*** .excludes | A list of Queue matches to exclude | n/a | exclude
federationConfigurations . ***name*** .queuePolicies . ***name*** .excludes . ***name*** .queueMatch | A Queue match pattern to apply. If none are present all queues will be matched | n/a | queue-match
federationConfigurations . ***name*** .queuePolicies . ***name*** .transformerRef | The ref name for a transformer (see transformer config) that you may wish to configure to transform the message on federation transfer. | n/a | transformer-ref
federationConfigurations . ***name*** .queuePolicies . ***name*** .includes |  | n/a | queue-match
federationConfigurations . ***name*** .queuePolicies . ***name*** .excludes . ***name*** .queueMatch | A Queue match pattern to apply. If none are present all queues will be matched | n/a | queue-match
federationConfigurations . ***name*** .queuePolicies . ***name*** .includeFederated | by default this is false, we don't federate a federated consumer, this is to avoid issue, where in symmetric               or any closed loop setup you could end up when no "real" consumers attached with messages flowing round and round endlessly. | n/a | include-federated
federationConfigurations . ***name*** .upstreamConfigurations |  | n/a | upstream
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration | This is the streams connection configuration | n/a | connection-configuration
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .priorityAdjustment |  | n/a | priority-adjustment
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .retryIntervalMultiplier | multiplier to apply to the retry-interval | 1 | retry-interval-multiplier
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .shareConnection | if there is a downstream and upstream connection configured for the same broker then                  the same connection will be shared as long as both stream configs set this flag to true | false | share-connection
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .maxRetryInterval | Maximum value for retry-interval | 2000 | max-retry-interval
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .connectionTTL |  | n/a | connection-t-t-l
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .circuitBreakerTimeout | whether this connection supports fail-over | 30000 | circuit-breaker-timeout
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .callTimeout | How long to wait for a reply | 30000 | call-timeout
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .staticConnectors | A list of connector references configured via connectors | n/a | static-connectors
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .reconnectAttempts | How many attempts should be made to reconnect after failure | -1 | reconnect-attempts
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .password | password, if unspecified the federated password is used | n/a | password
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .callFailoverTimeout | How long to wait for a reply if in the middle of a fail-over. -1 means wait forever. | -1 | call-failover-timeout
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .hA |  | n/a | h-a
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .initialConnectAttempts | How many attempts should be made to connect initially | -1 | initial-connect-attempts
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .retryInterval | period (in ms) between successive retries | 500 | retry-interval
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .clientFailureCheckPeriod |  | n/a | client-failure-check-period
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .connectionConfiguration .username |  | n/a | username
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .policyRefs |  | n/a | policy-refs
federationConfigurations . ***name*** .upstreamConfigurations . ***name*** .staticConnectors | A list of connector references configured via connectors | n/a | static-connectors
federationConfigurations . ***name*** .downstreamConfigurations |  | n/a | downstream
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration | This is the streams connection configuration | n/a | connection-configuration
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .priorityAdjustment |  | n/a | priority-adjustment
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .retryIntervalMultiplier | multiplier to apply to the retry-interval | 1 | retry-interval-multiplier
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .shareConnection | if there is a downstream and upstream connection configured for the same broker then                  the same connection will be shared as long as both stream configs set this flag to true | false | share-connection
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .maxRetryInterval | Maximum value for retry-interval | 2000 | max-retry-interval
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .connectionTTL |  | n/a | connection-t-t-l
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .circuitBreakerTimeout | whether this connection supports fail-over | 30000 | circuit-breaker-timeout
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .callTimeout | How long to wait for a reply | 30000 | call-timeout
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .staticConnectors | A list of connector references configured via connectors | n/a | static-connectors
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .reconnectAttempts | How many attempts should be made to reconnect after failure | -1 | reconnect-attempts
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .password | password, if unspecified the federated password is used | n/a | password
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .callFailoverTimeout | How long to wait for a reply if in the middle of a fail-over. -1 means wait forever. | -1 | call-failover-timeout
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .hA |  | n/a | h-a
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .initialConnectAttempts | How many attempts should be made to connect initially | -1 | initial-connect-attempts
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .retryInterval | period (in ms) between successive retries | 500 | retry-interval
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .clientFailureCheckPeriod |  | n/a | client-failure-check-period
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .connectionConfiguration .username |  | n/a | username
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .policyRefs |  | n/a | policy-refs
federationConfigurations . ***name*** .downstreamConfigurations . ***name*** .staticConnectors | A list of connector references configured via connectors | n/a | static-connectors
federationConfigurations . ***name*** .federationPolicys |  | n/a | policy-set
federationConfigurations . ***name*** .addressPolicies |  | n/a | address-policy
federationConfigurations . ***name*** .addressPolicies . ***name*** .autoDeleteMessageCount | The amount number messages in the upstream queue that the message count must be equal or below before the downstream broker has disconnected before the upstream queue can be eligable for auto-delete. | n/a | auto-delete-message-count
federationConfigurations . ***name*** .addressPolicies . ***name*** .enableDivertBindings | Setting to true will enable divert bindings to be listened for demand. If there is a divert binding with               an address that matches the included addresses for the stream, any queue bindings that match the forward address of the divert will create demand. Default is false | n/a | enable-divert-bindings
federationConfigurations . ***name*** .addressPolicies . ***name*** .includes.{NAME}.addressMatch |  | n/a | include
federationConfigurations . ***name*** .addressPolicies . ***name*** .maxHops | The number of hops that a message can have made for it to be federated | n/a | max-hops
federationConfigurations . ***name*** .addressPolicies . ***name*** .transformerRef | The ref name for a transformer (see transformer config) that you may wish to configure to transform the message on federation transfer. | n/a | transformer-ref
federationConfigurations . ***name*** .addressPolicies . ***name*** .autoDeleteDelay | The amount of time in milliseconds after the downstream broker has disconnected before the upstream queue can be eligable for auto-delete. | n/a | auto-delete-delay
federationConfigurations . ***name*** .addressPolicies . ***name*** .autoDelete | For address federation, the downstream dynamically creates a durable queue on the upstream address.               This is used to mark if the upstream queue should be deleted once downstream disconnects,               and the delay and message count params have been met. This is useful if you want to automate the clean up,               though you may wish to disable this if you want messages to queued for the downstream when disconnect no matter what. | n/a | auto-delete
federationConfigurations . ***name*** .addressPolicies . ***name*** .excludes.{NAME}.addressMatch |  | n/a | include
**clusterConfigurations** |  | 
clusterConfigurations . ***name*** .retryIntervalMultiplier | multiplier to apply to the retry-interval | 1 | retry-interval-multiplier
clusterConfigurations . ***name*** .maxRetryInterval | Maximum value for retry-interval | 2000 | max-retry-interval
clusterConfigurations . ***name*** .address | name of the address this cluster connection applies to | n/a | address
clusterConfigurations . ***name*** .maxHops | maximum number of hops cluster topology is propagated | 1 | max-hops
clusterConfigurations . ***name*** .connectionTTL | how long to keep a connection alive in the absence of any data arriving from the client | 60000 | connection-ttl
clusterConfigurations . ***name*** .clusterNotificationInterval | how often the cluster connection will notify the cluster of its existence right after joining the                  cluster | 1000 | notification-interval
clusterConfigurations . ***name*** .confirmationWindowSize | The size (in bytes) of the window used for confirming data from the server connected to.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 1048576 | confirmation-window-size
clusterConfigurations . ***name*** .callTimeout | How long to wait for a reply | 30000 | call-timeout
clusterConfigurations . ***name*** .staticConnectors | A list of connectors references names | n/a | static-connectors
clusterConfigurations . ***name*** .clusterNotificationAttempts | how many times this cluster connection will notify the cluster of its existence right after joining                  the cluster | 2 | notification-attempts
clusterConfigurations . ***name*** .allowDirectConnectionsOnly | restricts cluster connections to the listed connector-ref's | false | allow-direct-connections-only
clusterConfigurations . ***name*** .reconnectAttempts | How many attempts should be made to reconnect after failure | -1 | reconnect-attempts
clusterConfigurations . ***name*** .duplicateDetection | should duplicate detection headers be inserted in forwarded messages? | true | use-duplicate-detection
clusterConfigurations . ***name*** .callFailoverTimeout | How long to wait for a reply if in the middle of a fail-over. -1 means wait forever. | -1 | call-failover-timeout
clusterConfigurations . ***name*** .messageLoadBalancingType |  | n/a | message-load-balancing-type
clusterConfigurations . ***name*** .initialConnectAttempts | How many attempts should be made to connect initially | -1 | initial-connect-attempts
clusterConfigurations . ***name*** .connectorName | Name of the connector reference to use. | n/a | connector-ref
clusterConfigurations . ***name*** .retryInterval | period (in ms) between successive retries | 500 | retry-interval
clusterConfigurations . ***name*** .producerWindowSize | Producer flow control.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | 1048576 | producer-window-size
clusterConfigurations . ***name*** .clientFailureCheckPeriod |  | n/a | client-failure-check-period
clusterConfigurations . ***name*** .discoveryGroupName | name of discovery group used by this cluster-connection | n/a | discovery-group-name
clusterConfigurations . ***name*** .minLargeMessageSize | Messages larger than this are considered large-messages.                  Supports byte notation like "K", "Mb", "MiB", "GB", etc. | n/a | min-large-message-size
**connectionRouters** |  | 
connectionRouters . ***name*** .cacheConfiguration | This controls how often a cache removes its entries and if they are persisted. | n/a | cache
connectionRouters . ***name*** .cacheConfiguration .persisted | true means that the cache entries are persisted | false | persisted
connectionRouters . ***name*** .cacheConfiguration .timeout | the timeout (in milliseconds) before removing cache entries | -1 | timeout
connectionRouters . ***name*** .keyFilter | the filter for the target key | n/a | key-filter
connectionRouters . ***name*** .keyType | the optional target key | n/a | key-type
connectionRouters . ***name*** .localTargetFilter | the filter to get the local target | n/a | local-target-filter
connectionRouters . ***name*** .poolConfiguration | the pool configuration | n/a | pool
connectionRouters . ***name*** .poolConfiguration .quorumTimeout | the timeout (in milliseconds) used to get the minimum number of ready targets | 3000 | quorum-timeout
connectionRouters . ***name*** .poolConfiguration .password | the password to access the targets | n/a | password
connectionRouters . ***name*** .poolConfiguration .localTargetEnabled | true means that the local target is enabled | false | local-target-enabled
connectionRouters . ***name*** .poolConfiguration .checkPeriod | the period (in milliseconds) used to check if a target is ready | 5000 | check-period
connectionRouters . ***name*** .poolConfiguration .quorumSize | the minimum number of ready targets | 1 | quorum-size
connectionRouters . ***name*** .poolConfiguration .staticConnectors | A list of connector references configured via connectors | n/a | static-connectors
connectionRouters . ***name*** .poolConfiguration .discoveryGroupName | name of discovery group used by this bridge | n/a | discovery-group-name
connectionRouters . ***name*** .poolConfiguration .clusterConnection | the name of a cluster connection | n/a | cluster-connection
connectionRouters . ***name*** .poolConfiguration .username | the username to access the targets | n/a | username
connectionRouters . ***name*** .policyConfiguration |  | n/a | policy-configuration
connectionRouters . ***name*** ..properties.{PROPERTY} | A set of Key value pairs specific to each named property, see above description | n/a | property
