package com.topgaming.fusion_server.netty;

import com.topgaming.fusion_server.net.MessageDispatch;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

@ChannelHandler.Sharable
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(BootNettyChannelInboundHandlerAdapter.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("收到包体"+ msg.toString());
        MessageDispatch.Instance.receiveData(ctx,msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            cause.printStackTrace();
            ctx.flush();
        }catch (Exception e)
        {
            System.out.println("ExceptionCaught!");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        ctx.flush();
    }

    //连接处理
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress socket =(InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = socket.getAddress().getHostAddress();
        log.info("玩家(IP)"+clientIP+"连接到服务器！");

    }

    //断开处理
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress socket =(InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = socket.getAddress().getHostAddress();
        log.info("玩家(IP)"+clientIP+"断开服务器连接！");
        PlayerList.Instance.RemovePlayer(ctx.channel());
        ctx.flush();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered:"+ctx.channel().id());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered:"+ctx.channel().id());
    }
}
