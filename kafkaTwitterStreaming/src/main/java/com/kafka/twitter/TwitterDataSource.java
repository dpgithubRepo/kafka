package com.kafka.twitter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterDataSource {

	public static Client getTwitterClient(BlockingQueue<String> msgQueue) {
		Authentication hosebirdAuth = new OAuth1(getProperty("consumerKey"), getProperty("consumerSecret"),
				getProperty("token"), getProperty("secret"));
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		List<String> searchTerms = Lists.newArrayList(IKafkaTwitterConstants.SEARCH_TERMS);
		hosebirdEndpoint.trackTerms(searchTerms);
		ClientBuilder clientBuilder = new ClientBuilder().name("Hosebird-Client-01").hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));
		return clientBuilder.build();
		
	}

	private static String getProperty(String key) {
		return Configuration.getConfigurationValue(key);
	}
	
	public static void main(String[] args) {
		LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>(50000);
		getTwitterClient(messageQueue);
		
	}
}
