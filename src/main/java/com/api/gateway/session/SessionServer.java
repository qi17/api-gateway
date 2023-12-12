package com.api.gateway.session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;


/**
 * @author root
 * @description
 * @date 2023/11/19
 */
public class SessionServer implements Callable<Channel> {

    private final Logger logger = LoggerFactory.getLogger(SessionServer.class);

    private final EventLoopGroup boss = new NioEventLoopGroup(1);
    private final EventLoopGroup work = new NioEventLoopGroup();
    private Channel channel;

    private Configuration configuration;

    public SessionServer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //会话的初始信息
                    .childHandler(new SessionChannelInitializer(configuration));

            channelFuture = b.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error.", e);
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start done.");
            } else {
                logger.error("socket server start error.");
            }
        }
        return channel;

    }
}
