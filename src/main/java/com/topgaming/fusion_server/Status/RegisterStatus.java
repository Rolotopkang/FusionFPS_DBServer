package com.topgaming.fusion_server.Status;

/**
 * 注册状态
 */
public enum RegisterStatus
{
    /**
     * 注册成功
     */
    SUCCESS(0),

    /**
     * 注册失败原因为重复用户名
     */
    FAILED_SAMENAME(1),
    /**
     * 网络未连接
     */
    CONNECTION_FAILED(2);

    final int code;

    RegisterStatus(int i) {
        code =i;
    }

    public int getCode(){return code;}
}

