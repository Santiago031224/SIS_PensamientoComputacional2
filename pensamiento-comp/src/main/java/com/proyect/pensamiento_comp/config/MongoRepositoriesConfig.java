package com.proyect.pensamiento_comp.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ConditionalOnProperty(value = "app.mongo.enabled", havingValue = "true")
@EnableMongoRepositories(basePackages = "com.proyect.pensamiento_comp.documents.repository")
public class MongoRepositoriesConfig {
}
