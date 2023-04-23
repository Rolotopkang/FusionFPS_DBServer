package com.topgaming.fusion_server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootNettyServer {
    private static Logger log = LoggerFactory.getLogger(BootNettyServer.class);

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
            MyEncoder myEncoder = new MyEncoder();

            bootstrap = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(myEncoder);
                    channel.pipeline().addLast(new MyDecoder());
                    channel.pipeline().addLast(handlerAdapter);

                }

            });

            log.info("Netty 服务器启动成功");
            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        }catch (InterruptedException e)
        {
            log.info("错误" + e );
        }finally{
            log.error("释放线程池资源");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }



    }
}
