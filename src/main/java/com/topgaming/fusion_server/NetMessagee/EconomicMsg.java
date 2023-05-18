package com.topgaming.fusion_server.NetMessagee;

import lombok.Data;

@Data
public class EconomicMsg extends NetMessage{
    String username;
    int amount;
    int skinID;
}
