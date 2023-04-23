package com.topgaming.fusion_server.NetMessagee;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class NetMessage {

    int code;

    public String toJson()
    {
        return JSON.toJSONString(this);
    }
}
