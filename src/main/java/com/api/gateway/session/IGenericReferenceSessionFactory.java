package com.api.gateway.session;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author root
 * @description
 * @date 2023/11/19
 */
public interface IGenericReferenceSessionFactory {
    Future<Channel> openSession() throws ExecutionException, InterruptedException;
}
