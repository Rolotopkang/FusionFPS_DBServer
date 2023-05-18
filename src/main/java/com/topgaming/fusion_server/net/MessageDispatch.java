package com.topgaming.fusion_server.net;

import com.alibaba.fastjson2.JSON;
import com.topgaming.fusion_server.DataBase.DBOperate;
import com.topgaming.fusion_server.FusionServerApplication;
import com.topgaming.fusion_server.NetMessagee.*;
import com.topgaming.fusion_server.Status.DefaultStatus;
import com.topgaming.fusion_server.Status.LoginStatus;
import com.topgaming.fusion_server.Status.RegisterStatus;
import com.topgaming.fusion_server.netty.BootNettyChannelInboundHandlerAdapter;
import com.topgaming.fusion_server.netty.BootNettyServer;
import com.topgaming.fusion_server.netty.PlayerList;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.sql.SQLException;

import static com.topgaming.fusion_server.netty.HeartbeatHandler.HEARTBEAT_SEQUENCE;

public class MessageDispatch {
    public final static MessageDispatch Instance = new MessageDispatch();
    private static Logger log = LoggerFactory.getLogger(BootNettyChannelInboundHandlerAdapter.class);

    public void receiveData(ChannelHandlerContext ctx, Object rawMsg) throws SQLException {
        if (rawMsg.equals("heartbeat")) {
            return;
        }


        String netMsg = rawMsg.toString();
        NetMessage msg = JSON.parseObject(netMsg, NetMessage.class);
        InetSocketAddress socket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = socket.getAddress().getHostAddress();

        switch (NetCode.getType(msg.getCode())) {
            case None:
                log.warn("来自" + clientIP + "的未知请求！/" + msg.toString()+"/");
                break;
            case UserLogin:
                log.info("来自" + clientIP + "的登录请求！");
                LoginMsg LoginMsg = JSON.parseObject(netMsg, LoginMsg.class);
                LoginStatus loginStatus = DBOperate.getInstance().Login(LoginMsg.getUsername(), LoginMsg.getPassword());


                if (PlayerList.Instance.isOnline(LoginMsg.getUsername())) {
                    loginStatus = LoginStatus.ACCOUNT_ONLINE;
                } else if (loginStatus.equals(LoginStatus.SUCCESS)) {
                    PlayerList.Instance.AddPlayer(LoginMsg.getUsername(), ctx.channel());
                }


                log.info(clientIP + "登录请求:" + loginStatus.toString());
                //返回确认包
                LoginMsg LoginMsgReturn = new LoginMsg();
                LoginMsgReturn.setStatus(loginStatus.getCode());
                LoginMsgReturn.setCode(NetCode.UserLogin.getCode());
                ctx.writeAndFlush(LoginMsgReturn.toJson());

                break;
            case UserRegister:
                log.info("来自" + clientIP + "的注册请求！");
                RegisterMsg registerMsg = JSON.parseObject(netMsg, RegisterMsg.class);
                RegisterStatus registerStatus =
                        DBOperate.getInstance().register(registerMsg.getUsername(), registerMsg.getPassword());
                log.info("注册请求！" + registerStatus.toString());

                //返回确认包
                RegisterMsg RegisterReturn = new RegisterMsg();
                RegisterReturn.setStatus(registerStatus.getCode());
                RegisterReturn.setCode(NetCode.UserRegister.getCode());
                ctx.writeAndFlush(RegisterReturn.toJson());
                break;
            case UserMoneyGet:
                EconomicMsg economicMsg = JSON.parseObject(netMsg, EconomicMsg.class);
                log.info("来自" + clientIP +economicMsg.getUsername()+ "的查询余额请求！");
                int tmp_money = DBOperate.getInstance().UserMoneyGet(economicMsg.getUsername());
                log.info("查询余额请求！剩余：" + tmp_money);
                EconomicMsg economicReturn = new EconomicMsg();
                economicReturn.setAmount(tmp_money);
                economicReturn.setCode(NetCode.UserMoneyGet.getCode());
                economicReturn.setStatus(tmp_money==-1? DefaultStatus.FAILED.getCode() : DefaultStatus.SUCCESS.getCode());
                ctx.writeAndFlush(economicReturn.toJson());
                break;
            case UserMoneyAdd:
                EconomicMsg economicMsg1 = JSON.parseObject(netMsg, EconomicMsg.class);
                log.info("来自" + clientIP +economicMsg1.getUsername()+ "的金钱增加请求！");
                DefaultStatus defaultStatus = DBOperate.getInstance().UserMoneyAdd(economicMsg1.getUsername(),economicMsg1.getAmount());
                if(defaultStatus == DefaultStatus.SUCCESS)
                {
                    log.info("增加金钱成功");
                }else
                {
                    log.info("增加失败！");
                }
                NetMessage netMessage = new NetMessage();
                netMessage.setStatus(defaultStatus.getCode());
                netMessage.setCode(NetCode.UserMoneyAdd.getCode());
                ctx.writeAndFlush(netMessage.toJson());
                break;
            case VersionControl:
                EconomicMsg version = new EconomicMsg();
                log.info("来自" + clientIP +"的版本号验证");
                version.setUsername(FusionServerApplication.VERSION);
                version.setStatus(DefaultStatus.SUCCESS.getCode());
                version.setCode(NetCode.VersionControl.getCode());
                ctx.writeAndFlush(version.toJson());
                break;
        }
    }
}
