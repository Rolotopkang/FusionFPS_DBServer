package com.topgaming.fusion_server.netty;

import com.topgaming.fusion_server.net.MessageDispatch;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ChannelHandler.Sharable
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(BootNettyChannelInboundHandlerAdapter.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageDispatch.Instance.receiveData(ctx,msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开处理
        ctx.flush();
        ctx.close();
    }
}
