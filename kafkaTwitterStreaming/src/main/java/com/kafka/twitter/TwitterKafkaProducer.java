package com.kafka.twitter;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.hbc.core.Client;

public class TwitterKafkaProducer {

	static final Logger logger = LoggerFactory.getLogger(TwitterKafkaProducer.class);
	static BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>(50000);
	static Client hbClient;
	static KafkaProducer<String, String> producer;

	public void produce() {
		run(getKafkaProducer());
	}

	public static KafkaProducer<String, String> getKafkaProducer() {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaTwitterConstants.BOOTSRAP_SERVER);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producer = new KafkaProducer<String, String>(props);
		return producer;
	}

	public static void run(KafkaProducer<String, String> producer) {

		hbClient = TwitterDataSource.getTwitterClient(messageQueue);
		hbClient.connect();
		String message = null;
		while (!hbClient.isDone()) {
			try {
				message = messageQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error("Error while reading the message from messageQueue : {}", e.getMessage());
				hbClient.stop();
				throw new RuntimeException("Error while reading the message from messageQueue");

			}

			if (message != null && message.trim().length() > 0) {
				logger.info("< ===  Message received Start === >  ");
				logger.info(message);
				producer.send(new ProducerRecord<String, String>(IKafkaTwitterConstants.TWITTER_TOPIC, null, message),
						new Callback() {

							@Override
							public void onCompletion(RecordMetadata metadata, Exception e) {

								if (e != null) {
									logger.error("Error while producing the record to the topic: {}", e);
								}

							}
						});
				logger.info("< === Message End  === >");
			}

			// can test the exit process by returning from here
			// return;
		}

	}

	public static void exitProcess() {

		logger.info("*****Exiting the application*******");
		hbClient.stop();
		producer.close();
		logger.info("Application Exited !!");

	}

}
