package com.kafka.experiments;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherProducerWithKeys {
	
	private static Logger logger = LoggerFactory.getLogger(WeatherProducerWithKeys.class);
	
	public static void main(String[] args) {
		
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.BOOTSRAP_SERVER);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class.getName());
		
		// all the messages produced and sent with the same key will be written to the same partition of the topic
		String key = "weather_data";
		KafkaProducer<String, Double> producer = new KafkaProducer<String, Double>(props);
		ProducerRecord<String, Double> producerRecord = new ProducerRecord<String, Double>(IKafkaConstants.WEATHER_TOPIC, key, 34.0d);
		
		producer.send(producerRecord,new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception exception) {	
				
				if(exception == null) {
					logger.info("======= Record Meta data ========");
					logger.info("Message Produced to Topic: {} ", metadata.topic());
					logger.info("Messgae Produced to Partition: {}" ,  metadata.partition());
					logger.info("Messgae Produced to Offset: {} ", metadata.offset());
					logger.info("Messgae Produced at:{} ",new Date(metadata.timestamp()));
					
				}
			}
		});
		
		producer.flush();
		producer.close();
	}
	
}
