package com.topgaming.fusion_server.NetMessagee;

import com.alibaba.fastjson.JSON;

public class RegisterMsg extends NetMessage{
    String username;
    String password;
    String time;

    @Override
    public String toString()
    {
        return "UserLoginMsg{"+"code="+code + '\'' +
                ",password =" + password +"time ="+time+ '}';
    }
}
