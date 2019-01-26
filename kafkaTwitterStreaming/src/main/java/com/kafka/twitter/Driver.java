package com.kafka.twitter;

public class Driver {

	public static void main(String[] args) {
		TwitterKafkaProducer producer = new TwitterKafkaProducer();
		producer.produce();

		// handles the graceful shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			TwitterKafkaProducer.exitProcess();
		}));

	}

}
