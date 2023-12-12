package com.api.gateway.session.defaults;

import com.api.gateway.session.Configuration;
import com.api.gateway.session.IGenericReferenceSessionFactory;
import com.api.gateway.session.SessionServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author root
 * @description
 * @date 2023/11/19
 */
public class GenericReferenceSessionFactory implements IGenericReferenceSessionFactory {
    private final Logger logger = LoggerFactory.getLogger(GenericReferenceSessionFactory.class);
    private final Configuration configuration;

    public GenericReferenceSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Future<Channel> openSession() throws ExecutionException, InterruptedException {
        SessionServer sessionServer = new SessionServer(configuration);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(sessionServer);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");
        while (!channel.isActive()) {
            logger.info("netty server gateway start ing ...");
            Thread.sleep(500);
        }
        logger.info("netty server gateway start Done! {}", channel.localAddress());
        return future;
    }
}
