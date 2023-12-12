package com.api.gateway.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author root
 * @description 数据处理器基类
 * @date 2023/11/19
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, T t) throws Exception {
        session(channelHandlerContext, channelHandlerContext.channel(), t);
    }

    protected abstract void session(ChannelHandlerContext ctx, final Channel channel, T req);
}
