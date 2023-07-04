package com.topgaming.fusion_server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(BootNettyChannelInboundHandlerAdapter.class);
    // 客户端最后一次心跳时间戳
    private long lastHeartbeatTimestamp = 0;
    // 心跳消息内容
    public static final ByteBuf HEARTBEAT_SEQUENCE =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("heartbeat", CharsetUtil.UTF_8));
    //超时时间计算
    private static long OverTime = 10000;

    // 发送心跳包
    private void sendHeartbeat(ChannelHandlerContext ctx) {
        if (ctx.channel().isActive()) {
//            System.out.println("心跳发出!");
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
        }
    }

    // 处理心跳响应包
    private void handleHeartbeatResponse(ChannelHandlerContext ctx) {

        long currentTimestamp = System.currentTimeMillis();
        lastHeartbeatTimestamp = currentTimestamp;
//        System.out.println(
//                "Received heartbeat response from client " +
//                        ctx.channel().remoteAddress() +
//                        ", timestamp: " +
//                        currentTimestamp);
    }

    // 处理接收到的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.equals("heartbeat")) {
            handleHeartbeatResponse(ctx);
            ReferenceCountUtil.release(msg);
            return;
        }
        super.channelRead(ctx,msg);
    }

    // 连接建立时发送第一个心跳包
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        sendHeartbeat(ctx);
    }

    // 定时任务发送心跳包，判断客户端是否在线
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                sendHeartbeat(ctx);
            }
            if(event.state() == IdleState.ALL_IDLE)
            {
                long currentTimestamp = System.currentTimeMillis();
                System.out.println("检测超时时间！");
                if (currentTimestamp - lastHeartbeatTimestamp > OverTime) { // 超时时间为30秒
                    System.out.println("Client " + ctx.channel().remoteAddress() + " is offline");
                    PlayerList.Instance.RemovePlayer(ctx.channel());
                    ctx.close();
            }
            }
        }
    }
}
