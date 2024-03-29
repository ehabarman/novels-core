package com.itsmite.novels.core;

import com.itsmite.novels.core.repositories.ResourceRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@SpringBootApplication
@EnableMongoRepositories(repositoryBaseClass = ResourceRepositoryImpl.class)
@ImportResource()
public class CoreApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CoreApplication.class, args);
    }
}
