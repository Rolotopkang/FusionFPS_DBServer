package com.topgaming.fusion_server.netty;

import com.topgaming.fusion_server.net.MessageDispatch;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerList {
    public final static PlayerList Instance = new PlayerList();
    private final Map<String, Channel> OnlinePlayerList = new ConcurrentHashMap<>();

    public void AddPlayer(String name , Channel channel)
    {
//        System.out.println("增加"+name+":"+channel);
        OnlinePlayerList.put(name,channel);
    }

    public void RemovePlayer(Channel channel)
    {
        for (Map.Entry<String,Channel> entry : OnlinePlayerList.entrySet())
        {
            if(entry.getValue().equals(channel))
            {
//                System.out.println("移除"+entry.getKey()+":"+entry.getValue());
                OnlinePlayerList.remove(entry.getKey());
            }
        }
    }

    public Channel GetChannel(String name)
    {
        return OnlinePlayerList.get(name);
    }

    public boolean isOnline(String name)
    {
        return OnlinePlayerList.containsKey(name);
    }



}
