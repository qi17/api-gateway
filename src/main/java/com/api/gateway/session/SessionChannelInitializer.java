package com.api.gateway.session;

import com.api.gateway.session.handler.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author root
 * @description
 * @date 2023/11/19
 */
public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Configuration configuration;
    public SessionChannelInitializer(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new SessionServerHandler(configuration));
    }
}
