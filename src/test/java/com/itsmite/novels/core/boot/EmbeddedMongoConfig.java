package com.itsmite.novels.core.boot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongoCmdOptions;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@Profile("test")
@ActiveProfiles("test")
@Configuration
public class EmbeddedMongoConfig implements InitializingBean, DisposableBean {

    MongodExecutable mongodExecutable = null;

    @Override
    public void destroy() {
        mongodExecutable.stop();
    }

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create();
        System.out.println("============================================");
        System.out.println(mongoClient);
        System.out.println("============================================");
        return mongoClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IMongoCmdOptions cmdOptions = new MongoCmdOptionsBuilder()
            .useNoPrealloc(false)
            .useSmallFiles(false)
            .master(false)
            .verbose(false)
            .useNoJournal(false)
            .syncDelay(0)
            .build();
        IMongodConfig mongoConfig = new MongodConfigBuilder()
            .version(getVersion())
            .net(new Net(Network.getFreeServerPort(), Network.localhostIsIPv6()))
            .replication(new Storage(null, "testRepSet", 5000))
            .configServer(false)
            .cmdOptions(cmdOptions)
            .build();
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable executable = starter.prepare(mongoConfig);
        executable.start();
    }

    private IFeatureAwareVersion getVersion() {
        return Versions.withFeatures(new GenericVersion("3.0.0"), Version.Main.PRODUCTION.getFeatures());
    }
}