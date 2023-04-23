package com.topgaming.fusion_server.net;

import com.alibaba.fastjson2.JSON;
import com.topgaming.fusion_server.NetMessagee.NetCode;
import com.topgaming.fusion_server.NetMessagee.NetMessage;
import io.netty.channel.ChannelHandlerContext;

public class MessageDispatch {
    public final static MessageDispatch Instance = new MessageDispatch();

    public void receiveData(ChannelHandlerContext ctx,String netMsg)
    {
        NetMessage msg = JSON.parseObject(netMsg,NetMessage.class);
        switch (NetCode.getType(msg.getCode())){
            case None :
                System.out.println("请求代码错误！"+netMsg.toString());
                break;
            case UserLogin:
                System.out.println("登录请求！");
                break;
            case UserRegister:
                System.out.println("注册请求！");
                break;
        }
    }
}
