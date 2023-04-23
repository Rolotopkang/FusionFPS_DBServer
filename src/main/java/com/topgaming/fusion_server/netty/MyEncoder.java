package com.topgaming.fusion_server.netty;

import com.topgaming.fusion_server.NetMessagee.NetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class MyEncoder extends MessageToByteEncoder<NetMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NetMessage netMessage, ByteBuf byteBuf) throws Exception {
        if(netMessage == null)
            return;

        byte[] bytes = netMessage.toJson().getBytes(StandardCharsets.UTF_8);
        byte[] bytes1 = add4Bytes(bytes);
        byteBuf.writeBytes(bytes1);
    }

    public static byte[] add4Bytes(byte[] data2)
    {
        int num = data2.length;
        byte[] data1 = new byte[4];
        data1[0] = (byte) (num & 0xff);
        data1[1] = (byte) (num >> 8 & 0xff);
        data1[2] = (byte) (num >> 16 & 0xff);
        data1[3] = (byte) (num >> 24 & 0xff);

        byte[] data3 = new byte[data1.length + data2.length];

        System.arraycopy(data1,0,data3,0,data1.length);
        System.arraycopy(data2,0,data3,data1.length,data2.length);
        return data3;
    }
}
