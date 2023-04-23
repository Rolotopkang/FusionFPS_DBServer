package com.topgaming.fusion_server.NetMessagee;

import lombok.Data;

@Data
public class UserLoginMsg extends NetMessage{
    String username;
    String password;

    @Override
    public String toString()
    {
        return "UserLoginMsg{"+"code="+code + '\'' +
                ",password =" + password + '}';
    }
}
