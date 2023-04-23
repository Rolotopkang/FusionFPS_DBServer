package com.topgaming.fusion_server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        int preIndex = in.readerIndex();
        int length = readRawInt32(in);

        if(preIndex == in.readerIndex())
        {
            return;
        }

        if(length<0)
        {
            throw new CorruptedFrameException("长度错误！"+ length);
        }
        if(in.readableBytes() < length){
            in.resetReaderIndex();
        }else {
            byte[] body = new byte[length];
            in.readBytes(body);
            String str = new String(body);
            out.add(str);
        }
    }

    private static int readRawInt32(ByteBuf buffer){
        if(!buffer.isReadable())
        {
            return 0;
        }

        if(buffer.readableBytes()<4)
        {
            return 0;
        }

        buffer.markReaderIndex();

        int readInt = buffer.readInt();
        if(readInt<= 0)
        {
            buffer.resetReaderIndex();
            return 0;
        }else {
            return readInt;
        }
    }
}
