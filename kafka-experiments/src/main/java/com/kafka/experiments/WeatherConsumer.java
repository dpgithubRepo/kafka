package com.kafka.experiments;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WeatherConsumer {
	
	static Logger logger = LoggerFactory.getLogger(WeatherConsumer.class);
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.BOOTSRAP_SERVER);
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DoubleDeserializer.class.getName());
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID);
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,IKafkaConstants.EARLIEST_OFFSET);
		
		KafkaConsumer<String, Double> consumer = new KafkaConsumer<String, Double>(props);
		consumer.subscribe(Arrays.asList(IKafkaConstants.WEATHER_TOPIC));
		
		while(true) {
			ConsumerRecords<String, Double> records = consumer.poll(Duration.ofMillis(100));
			for(ConsumerRecord<String, Double> record: records) {
				logger.info("Key --> "+record.key());
				logger.info("Value --> "+record.value());
				logger.info("Offset --> "+record.offset());
				
			}
			
	}

}

}