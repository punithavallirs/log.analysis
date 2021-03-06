apache
KAFKA_ROOT=/data/kafka_2.11-0.10.0.0
$KAFKA_ROOT/bin/kafka-console-consumer.sh --zookeeper 192.85.247.104:2181,192.85.247.105:2181,192.85.247.106:2181 --topic log-analysis-topic --from-beginning
$KAFKA_ROOT/bin/kafka-console-producer.sh --broker-list 192.85.247.104:9092 --topic log-analysis-topic
$KAFKA_ROOT/bin/kafka-topics.sh --create --zookeeper 192.85.247.104:2181,192.85.247.105:2181,192.85.247.106:2181 --replication-factor 3 --partitions 3 --topic log-analysis-topic

oozie job -oozie http://192.85.247.104:11000/oozie/ -config /data/mtccore/sgs.job.properties -run
oozie job -oozie http://192.85.247.104:11000/oozie/ -config /data/mtccore/smsc.job.properties -run

hdp
KAFKA_ROOT=/usr/hdp/2.4.2.0-258/kafka
$KAFKA_ROOT/bin/kafka-topics.sh --create --zookeeper 192.85.246.17:2181 --replication-factor 1 --partitions 3 --topic log-analysis-topic
$KAFKA_ROOT/bin/kafka-console-consumer.sh --zookeeper 192.85.246.17:2181 --topic log-analysis-topic --from-beginning
$KAFKA_ROOT/bin/kafka-console-producer.sh --broker-list 192.85.246.17:6667 --topic log-analysis-topic

oozie job -oozie http://192.85.246.17:11000/oozie/ -config /data/mtccore/sgs.job.properties -run