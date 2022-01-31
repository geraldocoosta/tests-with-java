package com.example.brewery.util.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;


public class MongoDbContainerUtil {

	@Container
	public static MongoDBContainer container = new MongoDBContainer("mongo:5.0.5");

	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
	}

}
