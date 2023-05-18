package com.topgaming.fusion_server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BootNettyServer {
    private static Logger log = LoggerFactory.getLogger(BootNettyServer.class);
    private static int TimeOutSec = 5;



    public void bind(int port) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap = bootstrap.group(bossGroup,workerGroup);
            bootstrap = bootstrap.channel(NioServerSocketChannel.class);
            bootstrap = bootstrap.childOption(ChannelOption.TCP_NODELAY,true);

            BootNettyChannelInboundHandlerAdapter handlerAdapter = new BootNettyChannelInboundHandlerAdapter();
            HeartbeatHandler heartbeatHandler = new HeartbeatHandler();

            bootstrap = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {

                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                    channel.pipeline().addLast(new LengthFieldPrepender(4));
                    channel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                    channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));


                    channel.pipeline().addLast(new IdleStateHandler(TimeOutSec,0,TimeOutSec*2, TimeUnit.SECONDS));
                    channel.pipeline().addLast(heartbeatHandler);
                    channel.pipeline().addLast(handlerAdapter);


                }

            });

            log.info("Netty 服务器启动成功");
            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        }catch (InterruptedException e)
        {
            log.error("错误" + e );
        }finally{
            log.error("释放线程池资源");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }



    }
}
