package com.topgaming.fusion_server.Status;

/**
 * 登录状态
 */
public enum  LoginStatus
{
    /**
     * 网络未连接
     */
    CONNECTION_FAILED(-1),
    /**
     * 登录成功
     */
    SUCCESS(0),
    /**
     * 账号不存在
     */
    ID_NOT_EXIST(1),
    /**
     * 密码错误
     */
    PASSWORD_ERROR(2),
    /**
     * 账号已经登录
     */
    ACCOUNT_ONLINE(3),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(4);


    final int code;

    LoginStatus(int i) {
        code =i;
    }

    public int getCode(){return code;}
}
